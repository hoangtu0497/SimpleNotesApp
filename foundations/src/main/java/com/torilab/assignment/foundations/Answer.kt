package com.torilab.assignment.foundations

sealed class Answer<out D, out E> {
    data class Success<D>(val data: D) : Answer<D, Nothing>()

    data class Error<E>(val reason: E) : Answer<Nothing, E>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    fun <C> fold(
        success: (D) -> C,
        error: (E) -> C,
    ): C = when (this) {
        is Success -> success(data)
        is Error -> error(reason)
    }

    suspend fun <C> foldSuspend(
        success: suspend (D) -> C,
        error: suspend (E) -> C,
    ): C = when (this) {
        is Success -> success(data)
        is Error -> error(reason)
    }

    fun getOrNull(): D? = when (this) {
        is Success -> data
        else -> null
    }

    fun getError(): E? = when (this) {
        is Error -> reason
        else -> null
    }
}

inline fun <D, E> Answer<D, E>.onSuccess(action: (D) -> Unit): Answer<D, E> {
    if (this is Answer.Success) action(data)
    return this
}

inline fun <D, E> Answer<D, E>.onError(action: (E) -> Unit): Answer<D, E> {
    if (this is Answer.Error) action(reason)
    return this
}
