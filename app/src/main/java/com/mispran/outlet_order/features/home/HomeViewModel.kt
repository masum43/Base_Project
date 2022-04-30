package com.mispran.outlet_order.features.home

import androidx.lifecycle.*
import com.mispran.outlet_order.common.extensions.errorMessage
import com.mispran.outlet_order.common.network.model.Event
import com.mispran.outlet_order.common.network.model.ResponseState
import com.mispran.outlet_order.common.network.repos.MainRepository
import com.mispran.outlet_order.common.persistence.UserDataStore
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val mainRepository: MainRepository,
    private val dataStore: UserDataStore
): ViewModel() {

    private val _uiState = MutableLiveData<Event<ResponseState<Unit>>>()
    val uiState: LiveData<Event<ResponseState<Unit>>> get() = _uiState

    private val _api1DataList = MutableLiveData<ResponseState<FeedResponse>>()
    val api1DataList: LiveData<ResponseState<FeedResponse>> get() = _api1DataList

    fun fetchAnyApiData1() = viewModelScope.launch {
        try {
            _api1DataList.value = ResponseState.Loading
            val homeCategoryCourseResponse = mainRepository.fetchAnyApiData1()
            _api1DataList.value = ResponseState.Success(homeCategoryCourseResponse)
        } catch (e: Exception) {
            _api1DataList.value = ResponseState.Error(e.errorMessage)
        }
    }

    val fetchAnyApiData2 = liveData {
        try {
            emit(ResponseState.Loading)
            val response = mainRepository.fetchAnyApiData2()
            emit(ResponseState.Success(response))
        } catch (e: Exception) {
            Timber.e(e)
            emit(ResponseState.Error(e.errorMessage))
        }
    }
}