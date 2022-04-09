package com.mispran.outlet_order.common.network.api

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mispran.outlet_order.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://base_url.com/"

    private val loggingInterceptor = HttpLoggingInterceptor { log ->
        Timber.tag("REST-API").v(log)
    }.apply { level = HttpLoggingInterceptor.Level.BODY }

    private val moshi = Moshi.Builder().apply {
        add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
    }.build()

    private val okHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }
        addInterceptor(ResponseErrorInterceptor())
    }.build()
    
    val apiClient: ApiService = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        client(okHttpClient)
        addConverterFactory(MoshiConverterFactory.create())   
    }.build().create(ApiService::class.java)

    fun getApiClientWithChucker(context: Context): ApiService {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context).apply {
            maxContentLength(10000)
        }.build()
        val newOkHttpClient = okHttpClient.newBuilder()
            .addInterceptor(chuckerInterceptor)
            .build()
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(newOkHttpClient)
            addConverterFactory(MoshiConverterFactory.create(moshi))
        }.build().create(ApiService::class.java)
    }
    
}