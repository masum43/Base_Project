package com.mispran.outlet_order.features.home


import com.google.gson.annotations.SerializedName

data class FeedResponse(
    @SerializedName("current_page")
    val currentPage: String,
    @SerializedName("limit")
    val limit: String,
    @SerializedName("pages")
    val pages: String,
    @SerializedName("result")
    val result: List<Result>,
    @SerializedName("result_type")
    val resultType: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("total_results")
    val totalResults: String
) {
    data class Result(
        @SerializedName("article_id")
        val articleId: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("thumbnail")
        val thumbnail: String,
        @SerializedName("title")
        val title: String
    )
}