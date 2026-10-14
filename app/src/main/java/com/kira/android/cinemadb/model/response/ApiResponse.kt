package com.kira.android.cinemadb.model.response

sealed interface ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>
    data class Error(val message: String, val cause: Throwable? = null) : ApiResponse<Nothing>
    data object Loading : ApiResponse<Nothing>
}
