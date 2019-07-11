package com.feelsokman.stickers.ui.fragments.host.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.feelsokman.stickers.usecase.StickerPackLoaderUseCase

class HostViewModelFactory(private val stickerPackLoaderUseCase: StickerPackLoaderUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")

        return HostViewModel(stickerPackLoaderUseCase) as T
    }
}