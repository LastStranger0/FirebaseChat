package com.mtg.chatDomain.repository

import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.mtg.chatData.model.Message

class SendRepositoryImpl: SendRepository {
    override fun sendMessage(text: String?, name: String?, photoUrl: String?, imageUrl: String?, dbReference: DatabaseReference) {
        val message = Message(
            text, name, photoUrl, imageUrl
        )
        dbReference.child(MESSAGES_CHILD).push().setValue(message)
    }

    override fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?,
                                   auth: FirebaseAuth, dbReference: DatabaseReference, activity: AppCompatActivity) {
        storageReference.putFile(uri)
            .addOnSuccessListener(
                activity
            ) { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val friendlyMessage =
                            Message(null, getUserName(auth), getPhotoUrl(auth), uri.toString())
                        dbReference
                            .child(MESSAGES_CHILD)
                            .child(key!!)
                            .setValue(friendlyMessage)
                    }
            }
            .addOnFailureListener(activity) { e ->
                Log.w(
                    "PutImage",
                    "Image upload task was unsuccessful.",
                    e
                )
            }

    }

    override fun onImageSelected(uri: Uri, auth: FirebaseAuth, dbReference: DatabaseReference, activity: AppCompatActivity) {
        val user = auth.currentUser
        val tempMessage = Message(null, getUserName(auth), getPhotoUrl(auth), LOADING_IMAGE_URL)
        dbReference
            .child(MESSAGES_CHILD)
            .push()
            .setValue(
                tempMessage,
                DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError != null) {
                        Log.w(
                            "Database", "Unable to write message to database.",
                            databaseError.toException()
                        )
                        return@CompletionListener
                    }

                    val key = databaseReference.key
                    val storageReference = Firebase.storage
                        .getReference(user!!.uid)
                        .child(key!!)
                        .child(uri.lastPathSegment!!)
                    putImageInStorage(storageReference, uri, key, auth, dbReference, activity)
                })
    }


    override fun getPhotoUrl(auth: FirebaseAuth): String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString()
    }

    override fun getUserName(auth: FirebaseAuth): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else ANONYMOUS
    }

    companion object {
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}