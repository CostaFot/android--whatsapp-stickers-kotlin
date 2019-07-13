package com.costafot.stickers.ui.fragments.details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class DetailsViewModel : ViewModel() {

    val textData = MutableLiveData<String>()

    override fun onCleared() {
        Timber.tag("NavigationLogger").d("DetailsViewModel onCleared")
        super.onCleared()
    }
}