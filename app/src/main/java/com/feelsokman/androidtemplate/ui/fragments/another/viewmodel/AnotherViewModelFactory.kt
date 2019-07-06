package com.feelsokman.androidtemplate.ui.fragments.another.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.feelsokman.androidtemplate.usecase.GetStringFromStorageUseCase

class AnotherViewModelFactory(private val getStringFromStorageUseCase: GetStringFromStorageUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")

        return AnotherViewModel(getStringFromStorageUseCase) as T
    }
}