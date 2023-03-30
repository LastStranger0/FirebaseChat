package com.mtg.chatDomain.builder

import com.firebase.ui.auth.AuthUI

object IntentBuilder {
    fun buildSignInIntent() = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(
            listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
            )
        )
        .build()
}