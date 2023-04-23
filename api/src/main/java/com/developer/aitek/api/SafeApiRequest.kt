package com.developer.aitek.api

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>): T{
        val response = call.invoke()

        if (response.code() == 204){
            throw ApiException("204")
        }else {
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                @Suppress("BlockingMethodInNonBlockingContext")
                val error = response.errorBody()?.string()
                var message = ""

                error?.let {
                    message = try {
                        JSONObject(it).getString("message")
                    } catch (e: JSONException) {
                        "Error: (${response.code()})${response.message()}"
                    }
                }
                throw ApiException(message)
            }
        }
    }
}