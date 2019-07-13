package com.costafot.stickers.ui.fragments.host.viewmodel

import androidx.lifecycle.ViewModel
import timber.log.Timber

class HostViewModel() : ViewModel() {

    override fun onCleared() {
        Timber.tag("NavigationLogger").d("HostViewModel cleared")
        super.onCleared()
    }
}