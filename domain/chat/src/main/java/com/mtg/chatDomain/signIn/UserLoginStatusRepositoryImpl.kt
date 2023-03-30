package com.mtg.chatDomain.signIn

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.mtg.chatData.model.Result

class UserLoginStatusRepositoryImpl: UserLoginStatusRepository {
    override fun checkSignIn(
        context: Context,
        result: FirebaseAuthUIAuthenticationResult
    ): Result<Unit> {
        return try {
            if(result.resultCode == AppCompatActivity.RESULT_OK) {
                Result.Success(Unit)
            } else {
                Result.Error(result.idpResponse?.error!!)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun signOut(context: Context) {
        AuthUI.getInstance().signOut(context)
    }
}