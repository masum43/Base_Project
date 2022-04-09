package com.mispran.outlet_order.common.network.repos

import com.mispran.outlet_order.common.network.api.ApiService
import com.mispran.outlet_order.common.persistence.UserDataStore

class MainRepository(
    private val dataStore: UserDataStore,
    private val apiHelper: ApiService
) {

    private suspend fun token(): String = dataStore.getAuthToken() ?: ""



}