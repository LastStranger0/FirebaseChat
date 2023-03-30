package com.mtg.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mtg.chat.databinding.ActivityMainBinding
import com.mtg.chatDomain.adapter.MessageAdapter
import com.mtg.chatDomain.adapter.observer.BottomScrollObserver
import com.mtg.chatDomain.file.FileContract
import com.mtg.chatDomain.repository.SendRepository
import com.mtg.chatDomain.repository.SendRepositoryImpl
import com.mtg.chatDomain.repository.SendRepositoryImpl.Companion.MESSAGES_CHILD
import com.mtg.chatDomain.signIn.UserLoginStatusRepository
import com.mtg.chatDomain.signIn.UserLoginStatusRepositoryImpl
import com.mtg.chatDomain.ui.observer.SendButtonObserver
import com.mtg.chatDomain.useCase.PutImageUseCase
import com.mtg.chatDomain.useCase.SendUseCase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: LinearLayoutManager

    private val sendRepository: SendRepository = SendRepositoryImpl()
    private val sendUseCase = SendUseCase(sendRepository)
    private val putImageUseCase = PutImageUseCase(sendRepository)
    private val userLoginStatusRepository: UserLoginStatusRepository =
        UserLoginStatusRepositoryImpl()

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter

    private val openDocument = registerForActivityResult(FileContract()) { uri ->
        uri?.let { onImageSelected(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("10.0.2.2", 9001)
            Firebase.auth.useEmulator("10.0.2.2", 9011)
            Firebase.storage.useEmulator("10.0.2.2", 9119)
        }
        auth = Firebase.auth
        if (auth.currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        val options = MessageAdapter.realiseOptions(messagesRef)
        adapter = MessageAdapter(options, sendRepository.getUserName(auth))
        binding.progressBar.visibility = ProgressBar.INVISIBLE
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager
        binding.messageRecyclerView.adapter = adapter
        adapter.registerAdapterDataObserver(
            BottomScrollObserver(binding.messageRecyclerView, adapter, manager)
        )
        binding.messageEditText.addTextChangedListener(SendButtonObserver(binding.sendButton))
        binding.sendButton.setOnClickListener {
            sendUseCase.execute(
                binding.messageEditText.text.toString(),
                sendRepository.getUserName(auth),
                sendRepository.getPhotoUrl(auth),
                null,
                db.reference
            )
            binding.messageEditText.setText("")
        }
        binding.addMessageImageView.setOnClickListener {
            openDocument.launch(arrayOf("image/*"))
        }
    }


    public override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onImageSelected(uri: Uri) {
        Log.d(TAG, "Uri: $uri")
        putImageUseCase.execute(uri, auth, Firebase.database.reference, this)
    }

    private fun signOut() {
        userLoginStatusRepository.signOut(this)
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }


    companion object {
        private const val TAG = "MainActivity"

    }
}
