package com.manju23reddy.acronymsmeaning.util

sealed class Result<out T> {
    data class Success<R>(val data : R): Result<R>()
    data class Error<R>(val error : kotlin.Error) : Result<R>()
    object Loading : Result<Nothing>()
}