package com.costafot.stickers.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.costafot.stickers.usecase.ActionResolverUseCase
import com.costafot.stickers.usecase.IntentResolverUseCase
import com.costafot.stickers.usecase.StickerPackLoaderUseCase

class MainViewModelFactory(
    private val stickerPackLoaderUseCase: StickerPackLoaderUseCase,
    private val intentResolverUseCase: IntentResolverUseCase,
    private val actionResolverUseCase: ActionResolverUseCase
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")

        return MainViewModel(stickerPackLoaderUseCase, intentResolverUseCase, actionResolverUseCase) as T
    }
}