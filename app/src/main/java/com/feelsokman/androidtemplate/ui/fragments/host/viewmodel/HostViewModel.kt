package com.feelsokman.androidtemplate.ui.fragments.host.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import java.util.UUID

class HostViewModel : ViewModel() {

    val textData: MutableLiveData<String> =
        MutableLiveData<String>().apply { postValue(UUID.randomUUID().toString()) }

    fun changeText() {
        textData.postValue(UUID.randomUUID().toString())
    }

    override fun onCleared() {
        Timber.tag("NavigationLogger").d("HostViewModel cleared")
        super.onCleared()
    }
}