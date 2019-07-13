package com.costafot.stickers.ui.fragments.host.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.costafot.stickers.contentprovider.model.StickerPack
import com.costafot.stickers.ui.SingleLiveEvent
import com.costafot.stickers.usecase.BaseDisposableUseCase
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import com.costafot.stickers.usecase.error.DataSourceError
import timber.log.Timber

class HostViewModel(private val stickerPackLoaderUseCase: StickerPackLoaderUseCase) : ViewModel() {

    val stickerData = MutableLiveData<ArrayList<StickerPack>>()
    val errorMessage = SingleLiveEvent<String>()

    override fun onCleared() {
        Timber.tag("NavigationLogger").d("HostViewModel cleared")
        super.onCleared()
    }

    fun loadStickers() {

        stickerPackLoaderUseCase.loadStickerPacks(object : BaseDisposableUseCase.Callback<ArrayList<StickerPack>> {
            override fun onLoadingStarted() {
                //
            }

            override fun onSuccess(result: ArrayList<StickerPack>) {
                stickerData.postValue(result)
                //
            }

            override fun onError(error: DataSourceError) {
                errorMessage.value = error.errorMessage
            }
        })
    }
}