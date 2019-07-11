package com.feelsokman.stickers.ui.fragments.another.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class AnotherViewModel : ViewModel() {

    val textData = MutableLiveData<String>()

    override fun onCleared() {
        Timber.tag("NavigationLogger").d("AnotherViewModel onCleared")
        super.onCleared()
    }
}