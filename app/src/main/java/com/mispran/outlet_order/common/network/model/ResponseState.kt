package com.mispran.outlet_order.common.network.model

sealed class ResponseState<out T> {
    object Loading: ResponseState<Nothing>()
    data class Error(var errorMessage: String? = null): ResponseState<Nothing>()
    data class Success<T>(val data: T): ResponseState<T>()
}
