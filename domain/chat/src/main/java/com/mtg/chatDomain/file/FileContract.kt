package com.mtg.chatDomain.file

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class FileContract: ActivityResultContracts.OpenDocument() {

    override fun createIntent(context: Context, input: Array<String>): Intent {
        val intent = super.createIntent(context, input)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        return intent
    }
}