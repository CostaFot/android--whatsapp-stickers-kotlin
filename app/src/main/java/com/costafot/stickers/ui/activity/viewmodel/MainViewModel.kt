package com.costafot.stickers.ui.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.costafot.stickers.contentprovider.model.StickerPack
import com.costafot.stickers.ui.SingleLiveEvent
import com.costafot.stickers.usecase.BaseDisposableUseCase
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import com.costafot.stickers.usecase.error.DataSourceError
import timber.log.Timber
import java.util.UUID

class MainViewModel(private val stickerPackLoaderUseCase: StickerPackLoaderUseCase) : ViewModel() {

    val textData = MutableLiveData<String>().apply { postValue(UUID.randomUUID().toString()) }

    val stickerData = MutableLiveData<ArrayList<StickerPack>>()
    val errorMessage = SingleLiveEvent<String>()

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

    override fun onCleared() {
        Timber.d("MainViewModel cleared")
        super.onCleared()
    }
}