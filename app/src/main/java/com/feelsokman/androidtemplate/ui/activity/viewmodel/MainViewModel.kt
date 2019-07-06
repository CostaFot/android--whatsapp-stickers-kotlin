package com.feelsokman.androidtemplate.ui.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feelsokman.androidtemplate.usecase.GetStringFromStorageUseCase
import timber.log.Timber
import java.util.UUID

class MainViewModel(private val getStringFromStorageUseCase: GetStringFromStorageUseCase) :
    ViewModel() {

    val textData = MutableLiveData<String>().apply { postValue(UUID.randomUUID().toString()) }

    override fun onCleared() {
        Timber.d("MainViewModel cleared")
        super.onCleared()
    }
}