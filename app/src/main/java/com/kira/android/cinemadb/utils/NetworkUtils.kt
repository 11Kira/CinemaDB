package com.kira.android.cinemadb.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

class NetworkUtils {
    fun parseNetworkError(e: Exception): String {
        return when (e) {
            is HttpException -> {
                try {
                    val errorBody = e.response()?.errorBody()?.string()

                    if (!errorBody.isNullOrBlank()) {
                        val jsonElement = Json.parseToJsonElement(errorBody)
                        jsonElement.jsonObject["message"]?.jsonPrimitive?.content
                            ?: "An unexpected server error occurred."
                    } else {
                        "An unexpected server error occurred."
                    }
                } catch (parseException: Exception) {
                    "Failed to parse server response."
                }
            }

            is UnknownHostException, is ConnectException -> {
                "Network error. Please check your internet connection."
            }

            is InterruptedIOException -> {
                "The server took too long to respond. Please try again."
            }

            is IOException -> {
                "A network error occurred while communicating with the server."
            }

            else -> {
                e.message ?: "An unknown error occurred."
            }
        }
    }
}