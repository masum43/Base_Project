package com.mispran.outlet_order.common.network.api

import com.mispran.outlet_order.features.home.FeedResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {

    @GET("api/endPoint")
    suspend fun fetchAnyApiData1(
        @Header("Authorization") token: String): FeedResponse

    @GET("api/endPoint")
    suspend fun fetchAnyApiData2(
        @Header("Authorization") token: String): FeedResponse
}