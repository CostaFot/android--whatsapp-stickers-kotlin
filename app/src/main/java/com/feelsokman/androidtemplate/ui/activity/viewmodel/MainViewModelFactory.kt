package com.feelsokman.androidtemplate.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.feelsokman.androidtemplate.usecase.GetStringFromStorageUseCase

class MainViewModelFactory(private val getStringFromStorageUseCase: GetStringFromStorageUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")

        return MainViewModel(getStringFromStorageUseCase) as T
    }
}