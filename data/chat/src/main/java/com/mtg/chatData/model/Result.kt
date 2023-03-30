package com.mtg.chatData.model

sealed interface Result<out T>{
    fun isSuccess() = this is Success

    class Success<T>(val succeeded: T): Result<T>
    class Error(val exception: Throwable? = null): Result<Nothing>
}
