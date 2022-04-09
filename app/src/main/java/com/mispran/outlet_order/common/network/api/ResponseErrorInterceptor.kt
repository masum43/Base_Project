package com.mispran.outlet_order.common.network.api

import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ResponseErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return if (response.isSuccessful) {
            response
        } else {

            val errorMessage = try {
                val errorBody = JSONObject(response.peekBody(2048).string())
                errorBody.getJSONArray("msg").getString(0)
            } catch (e: Exception) {
                null
            }

            if (errorMessage != null) {
                throw IOException(errorMessage)
            } else {
                return response
            }
        }
    }

}