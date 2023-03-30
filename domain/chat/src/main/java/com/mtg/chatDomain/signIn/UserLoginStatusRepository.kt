package com.mtg.chatDomain.signIn

import android.content.Context
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.mtg.chatData.model.Result

interface UserLoginStatusRepository {
    fun checkSignIn(context: Context, result: FirebaseAuthUIAuthenticationResult): Result<Unit>
    fun signOut(context: Context)
}