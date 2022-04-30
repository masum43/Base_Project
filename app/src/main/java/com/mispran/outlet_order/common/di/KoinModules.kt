package com.mispran.outlet_order.common.di

import com.mispran.outlet_order.common.network.api.RetrofitClient
import com.mispran.outlet_order.common.network.repos.MainRepository
import com.mispran.outlet_order.common.persistence.UserDataStore
import com.mispran.outlet_order.common.utils.Utils
import com.mispran.outlet_order.features.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { Utils(androidApplication()) }

    single { UserDataStore(androidApplication()) }

    single {
        RetrofitClient.getApiClientWithChucker(androidContext())
    }

    single { MainRepository(get(), get()) }

    // ViewModels
    viewModel<HomeViewModel>()
}