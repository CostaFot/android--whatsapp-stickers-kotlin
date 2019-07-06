package com.feelsokman.androidtemplate.ui.fragments.another.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feelsokman.androidtemplate.usecase.GetStringFromStorageUseCase
import com.feelsokman.net.domain.error.DataSourceError
import com.feelsokman.net.domain.usecases.BaseDisposableUseCase
import timber.log.Timber

class AnotherViewModel(private val getStringFromStorageUseCase: GetStringFromStorageUseCase) : ViewModel() {

    val textData = MutableLiveData<String>()

    fun observeStringFromStorage() {
        if (textData.value == null) {
            getStringFromStorageUseCase.getStringFromStorage(object : BaseDisposableUseCase.Callback<String> {
                override fun onLoadingStarted() {
                    // TODO
                }

                override fun onSuccess(result: String) {
                    textData.postValue(result)
                }

                override fun onError(error: DataSourceError) {
                    textData.postValue(error.errorMessage)
                }
            })
        }
    }

    override fun onCleared() {
        getStringFromStorageUseCase.stopAllBackgroundWork()
        Timber.tag("NavigationLogger").d("AnotherViewModel onCleared")
        super.onCleared()
    }
}