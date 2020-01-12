package com.costafot.stickers.ui.fragments.details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.costafot.stickers.extensions.logDebug

class DetailsViewModel : ViewModel() {

    val textData = MutableLiveData<String>()

    override fun onCleared() {
        logDebug { "DetailsViewModel onCleared" }
        super.onCleared()
    }
}
