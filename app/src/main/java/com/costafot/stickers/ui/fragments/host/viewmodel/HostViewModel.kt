package com.costafot.stickers.ui.fragments.host.viewmodel

import androidx.lifecycle.ViewModel
import com.costafot.stickers.extensions.logDebug

class HostViewModel : ViewModel() {

    override fun onCleared() {
        logDebug { "HostViewModel cleared" }
        super.onCleared()
    }
}
