package com.mtg.chatDomain.repository

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

interface SendRepository {
    fun sendMessage(
        text: String? = null,
        name: String? = null,
        photoUrl: String? = null,
        imageUrl: String? = null,
        dbReference: DatabaseReference
    )

    fun putImageInStorage(
        storageReference: StorageReference,
        uri: Uri,
        key: String?,
        auth: FirebaseAuth,
        dbReference: DatabaseReference,
        activity: AppCompatActivity
    )

    fun onImageSelected(
        uri: Uri, auth: FirebaseAuth, dbReference: DatabaseReference, activity: AppCompatActivity
    )

    fun getUserName(auth: FirebaseAuth): String?

    fun getPhotoUrl(auth: FirebaseAuth): String?
}