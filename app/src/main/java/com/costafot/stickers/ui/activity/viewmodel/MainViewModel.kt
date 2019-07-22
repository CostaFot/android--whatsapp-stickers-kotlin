package com.costafot.stickers.ui.activity.viewmodel

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.costafot.stickers.R
import com.costafot.stickers.contentprovider.model.StickerPack
import com.costafot.stickers.ui.SingleLiveEvent
import com.costafot.stickers.ui.activity.LaunchIntentCommand
import com.costafot.stickers.usecase.ActionResolverUseCase
import com.costafot.stickers.usecase.AddStickerPackActions
import com.costafot.stickers.usecase.AddStickerPackActions.ADD_TO_BUSINESS
import com.costafot.stickers.usecase.AddStickerPackActions.ADD_TO_CONSUMER
import com.costafot.stickers.usecase.AddStickerPackActions.APPS_NOT_FOUND
import com.costafot.stickers.usecase.AddStickerPackActions.ASK_USER_WHICH_APP
import com.costafot.stickers.usecase.AddStickerPackActions.PROMPT_UPDATE_CAUSE_FAILURE
import com.costafot.stickers.usecase.BaseDisposableUseCase
import com.costafot.stickers.usecase.IntentResolverUseCase
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import com.costafot.stickers.usecase.WhiteListCheckUseCase
import com.costafot.stickers.usecase.error.DataSourceError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel(
    private val stickerPackLoaderUseCase: StickerPackLoaderUseCase,
    private val intentResolverUseCase: IntentResolverUseCase,
    private val actionResolverUseCase: ActionResolverUseCase
) : ViewModel() {

    val toastSingleLiveEvent = SingleLiveEvent<Int>()
    val launchIntentSingleLiveEvent = SingleLiveEvent<LaunchIntentCommand>()
    val stickerData = MutableLiveData<ArrayList<StickerPack>>()
    val detailsStickerPackData = MutableLiveData<StickerPack>()
    val errorMessage = SingleLiveEvent<String>()

    var currentDetailsPosition = 0

    fun doSomething() {
        Timber.d("Before coroutine")
        viewModelScope.launch {
            Timber.d("Start coroutine")
            val error = bg()
            Timber.d("End of suspend function")
            errorMessage.value = error
        }

        Timber.d("Outside coroutine")
    }

    suspend fun bg(): String {
        return withContext(Dispatchers.Default) {
            Timber.d("Inside the suspend function")
            "balls"
        }
    }

    fun loadStickers() {
        viewModelScope.launch {
            try {
                stickerData.value = stickerPackLoaderUseCase.loadStickerPacksSuspended()
                updateDetailsStickerPack(currentDetailsPosition)
            } catch (e: Exception) {
                errorMessage.value = e.localizedMessage
            }
        }

        /*stickerPackLoaderUseCase.loadStickerPacks(object : BaseDisposableUseCase.Callback<ArrayList<StickerPack>> {
            override fun onLoadingStarted() {
                // TODO
            }

            override fun onSuccess(result: ArrayList<StickerPack>) {
                stickerData.value = result
                updateDetailsStickerPack(currentDetailsPosition)
            }

            override fun onError(error: DataSourceError) {
                errorMessage.value = error.errorMessage
            }
        })*/
    }

    fun tryToAddStickerPack(identifier: String, packName: String) {

        actionResolverUseCase.resolveActionAddStickerPack(
            identifier,
            object : BaseDisposableUseCase.Callback<AddStickerPackActions> {
                override fun onLoadingStarted() {
                    // No Op
                }

                override fun onSuccess(result: AddStickerPackActions) {
                    when (result) {
                        APPS_NOT_FOUND -> {
                            toastSingleLiveEvent.value = R.string.add_pack_fail_prompt_update_whatsapp
                        }
                        ASK_USER_WHICH_APP -> {
                            launchIntentSingleLiveEvent.value = LaunchIntentCommand.Chooser(identifier, packName)
                        }
                        ADD_TO_CONSUMER -> {
                            launchIntentSingleLiveEvent.value =
                                LaunchIntentCommand.Specific(
                                    identifier,
                                    packName,
                                    WhiteListCheckUseCase.CONSUMER_WHATSAPP_PACKAGE_NAME
                                )
                        }
                        ADD_TO_BUSINESS -> {
                            launchIntentSingleLiveEvent.value =
                                LaunchIntentCommand.Specific(
                                    identifier,
                                    packName,
                                    WhiteListCheckUseCase.SMB_WHATSAPP_PACKAGE_NAME
                                )
                        }
                        PROMPT_UPDATE_CAUSE_FAILURE -> {
                            toastSingleLiveEvent.value = R.string.add_pack_fail_prompt_update_whatsapp
                        }
                    }
                }

                override fun onError(error: DataSourceError) {
                    toastSingleLiveEvent.value = R.string.add_pack_fail_prompt_update_whatsapp
                }
            })
    }

    fun getIntentToAddStickerPack(identifier: String, packName: String): Intent {
        return intentResolverUseCase.createIntentToAddStickerPack(identifier, packName)
    }

    fun updateDetailsStickerPack(position: Int) {
        currentDetailsPosition = position
        detailsStickerPackData.postValue(stickerData.value?.get(currentDetailsPosition))
    }

    override fun onCleared() {
        // need to stop all background threads
        stickerPackLoaderUseCase.stopAllBackgroundWork()
        actionResolverUseCase.stopAllBackgroundWork()
        Timber.d("MainViewModel cleared")
        super.onCleared()
    }
}