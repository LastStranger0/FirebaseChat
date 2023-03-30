package com.mtg.chatDomain.useCase

import com.google.firebase.database.DatabaseReference
import com.mtg.chatDomain.repository.SendRepository

class SendUseCase(
    private val sendRepository: SendRepository
) {
    fun execute(
        text: String?,
        name: String?,
        photoUrl: String?,
        imageUrl: String?,
        dbReference: DatabaseReference
    ) {
        sendRepository.sendMessage(text, name, photoUrl, imageUrl, dbReference)
    }
}