package com.costafot.stickers.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.costafot.stickers.usecase.IntentResolverUseCase
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import com.costafot.stickers.usecase.WhiteListCheckUseCase

class MainViewModelFactory(
    private val stickerPackLoaderUseCase: StickerPackLoaderUseCase,
    private val whiteListCheckUseCase: WhiteListCheckUseCase,
    private val intentResolverUseCase: IntentResolverUseCase
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")

        return MainViewModel(stickerPackLoaderUseCase, whiteListCheckUseCase, intentResolverUseCase) as T
    }
}