package com.costafot.stickers.ui.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costafot.stickers.R
import com.costafot.stickers.contentprovider.model.StickerPack
import com.costafot.stickers.model.ToastMessage
import com.costafot.stickers.ui.SingleLiveEvent
import com.costafot.stickers.ui.activity.LaunchIntentCommand
import com.costafot.stickers.usecase.ActionResolverUseCase
import com.costafot.stickers.usecase.AddStickerPackAction.ADD_TO_BUSINESS
import com.costafot.stickers.usecase.AddStickerPackAction.ADD_TO_CONSUMER
import com.costafot.stickers.usecase.AddStickerPackAction.APPS_NOT_FOUND
import com.costafot.stickers.usecase.AddStickerPackAction.ASK_USER_WHICH_APP
import com.costafot.stickers.usecase.AddStickerPackAction.PROMPT_UPDATE_CAUSE_FAILURE
import com.costafot.stickers.usecase.IntentResolverUseCase
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val stickerPackLoaderUseCase: StickerPackLoaderUseCase,
    private val intentResolverUseCase: IntentResolverUseCase,
    private val actionResolverUseCase: ActionResolverUseCase
) : ViewModel() {

    val toastSingleLiveEvent = SingleLiveEvent<ToastMessage>()
    val launchIntentSingleLiveEvent = SingleLiveEvent<LaunchIntentCommand>()
    val stickerData = MutableLiveData<ArrayList<StickerPack>>()
    val detailsStickerPackData = MutableLiveData<StickerPack>()

    private var currentDetailsPosition = 0

    fun loadStickers() {
        viewModelScope.launch {
            try {
                val stickerPacks: ArrayList<StickerPack> = stickerPackLoaderUseCase.loadStickerPacks()
                stickerData.value = stickerPacks
                updateDetailsStickerPack(currentDetailsPosition)
            } catch (e: Exception) {
                toastSingleLiveEvent.value = ToastMessage(message = e.localizedMessage)
            }
        }
    }

    fun tryToAddStickerPack(identifier: String, packName: String) {
        viewModelScope.launch {
            try {
                when (actionResolverUseCase.resolveActionAddStickerPack(identifier)) {
                    APPS_NOT_FOUND -> {
                        toastSingleLiveEvent.value = ToastMessage(resourceId = R.string.add_pack_fail_prompt_update_whatsapp)
                    }
                    ASK_USER_WHICH_APP -> {
                        val intent = intentResolverUseCase.createChooserIntentToAddStickerPack(identifier, packName)
                        launchIntentSingleLiveEvent.value = LaunchIntentCommand.Chooser(intent)
                    }
                    ADD_TO_CONSUMER -> {
                        val intent = intentResolverUseCase.createConsumerIntentToAddStickerPack(identifier, packName)
                        launchIntentSingleLiveEvent.value = LaunchIntentCommand.Specific(intent)
                    }
                    ADD_TO_BUSINESS -> {
                        val intent = intentResolverUseCase.createBusinessIntentToAddStickerPack(identifier, packName)
                        launchIntentSingleLiveEvent.value = LaunchIntentCommand.Specific(intent)
                    }
                    PROMPT_UPDATE_CAUSE_FAILURE -> {
                        toastSingleLiveEvent.value = ToastMessage(resourceId = R.string.add_pack_fail_prompt_update_whatsapp)
                    }
                }
            } catch (e: Exception) {
                toastSingleLiveEvent.value = ToastMessage(resourceId = R.string.add_pack_fail_prompt_update_whatsapp)
            }
        }
    }

    fun updateDetailsStickerPack(position: Int) {
        currentDetailsPosition = position
        detailsStickerPackData.postValue(stickerData.value?.get(currentDetailsPosition))
    }

    override fun onCleared() {
        Timber.d("MainViewModel cleared")
        super.onCleared()
    }
}
