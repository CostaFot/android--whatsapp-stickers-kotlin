package com.costafot.stickers.ui.activity.viewmodel

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.costafot.stickers.R
import com.costafot.stickers.contentprovider.model.StickerPack
import com.costafot.stickers.ui.SingleLiveEvent
import com.costafot.stickers.ui.activity.LaunchIntentContainer
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
import timber.log.Timber

class MainViewModel(
    private val stickerPackLoaderUseCase: StickerPackLoaderUseCase,
    private val whiteListCheckUseCase: WhiteListCheckUseCase,
    private val intentResolverUseCase: IntentResolverUseCase
) : ViewModel() {

    val toastSingleLiveEvent = SingleLiveEvent<Int>()
    val launchIntentSingleLiveEvent = SingleLiveEvent<LaunchIntentContainer>()
    val stickerData = MutableLiveData<ArrayList<StickerPack>>()
    val errorMessage = SingleLiveEvent<String>()

    fun loadStickers() {

        stickerPackLoaderUseCase.loadStickerPacks(object : BaseDisposableUseCase.Callback<ArrayList<StickerPack>> {
            override fun onLoadingStarted() {
                // TODO
            }

            override fun onSuccess(result: ArrayList<StickerPack>) {
                stickerData.postValue(result)
            }

            override fun onError(error: DataSourceError) {
                errorMessage.value = error.errorMessage
            }
        })
    }

    fun tryToAddStickerPack(identifier: String, packName: String) {

        whiteListCheckUseCase.resolveActionAddStickerPack(
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
                            launchIntentSingleLiveEvent.value = LaunchIntentContainer.Chooser(identifier, packName)
                        }
                        ADD_TO_CONSUMER -> {
                            launchIntentSingleLiveEvent.value =
                                LaunchIntentContainer.Specific(
                                    identifier,
                                    packName,
                                    WhiteListCheckUseCase.CONSUMER_WHATSAPP_PACKAGE_NAME
                                )
                        }
                        ADD_TO_BUSINESS -> {
                            launchIntentSingleLiveEvent.value =
                                LaunchIntentContainer.Specific(
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

    override fun onCleared() {
        stickerPackLoaderUseCase.stopAllBackgroundWork()
        whiteListCheckUseCase.stopAllBackgroundWork()
        Timber.d("MainViewModel cleared")
        super.onCleared()
    }
}