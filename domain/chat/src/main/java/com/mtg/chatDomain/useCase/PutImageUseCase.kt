package com.mtg.chatDomain.useCase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mtg.chatDomain.repository.SendRepository

class PutImageUseCase(
    private val sendRepository: SendRepository
) {
    fun execute(uri: Uri, auth: FirebaseAuth, dbReference: DatabaseReference, activity: AppCompatActivity){
        sendRepository.onImageSelected(uri, auth, dbReference, activity)
    }
}