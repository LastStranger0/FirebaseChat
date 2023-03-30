package com.mtg.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mtg.chat.databinding.ActivitySignInBinding
import com.mtg.chatDomain.builder.IntentBuilder
import com.mtg.chatDomain.signIn.UserLoginStatusRepositoryImpl

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val userLoginStatusRepository = UserLoginStatusRepositoryImpl()

    private val signIn: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract(), this::onSignInResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    public override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser == null) {
            val signInIntent = IntentBuilder.buildSignInIntent()
            signIn.launch(signInIntent)
        } else {
            goToMainActivity()
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val resultChecked = userLoginStatusRepository.checkSignIn(this, result)
        if (resultChecked.isSuccess()) {
            goToMainActivity()
        } else {
            Toast.makeText(
                this,
                "Error with a signing in",
                Toast.LENGTH_LONG
            ).show()

            val response = result.idpResponse
            if (response == null) {
                Log.w(TAG, "Sign in canceled")
            } else {
                Log.w(TAG, "Sign in error", response.error)
            }
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        private const val TAG = "SignInActivity"
    }
}