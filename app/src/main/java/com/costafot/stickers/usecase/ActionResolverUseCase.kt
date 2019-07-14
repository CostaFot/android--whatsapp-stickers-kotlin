package com.costafot.stickers.usecase

import com.costafot.stickers.usecase.error.DataSourceError
import com.costafot.stickers.usecase.error.DataSourceErrorKind
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class ActionResolverUseCase(
    private val scheduler: Scheduler,
    private val whiteListCheckUseCase: WhiteListCheckUseCase
) : BaseDisposableUseCase() {

    override val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override var latestDisposable: Disposable? = null

    fun resolveActionAddStickerPack(identifier: String, callback: Callback<AddStickerPackActions>) {
        latestDisposable?.dispose()
        latestDisposable =
            Single.fromCallable { resolve(identifier) }
                .subscribeOn(scheduler)
                .doOnSubscribe { compositeDisposable.add(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { action -> callback.onSuccess(action) },
                    { error -> callback.onError(DataSourceError(error.localizedMessage, DataSourceErrorKind.UNEXPECTED)) }
                )
    }

    private fun resolve(identifier: String): AddStickerPackActions {
        // if neither WhatsApp Consumer or WhatsApp Business is installed, then tell user to install the apps.
        if (!whiteListCheckUseCase.isWhatsAppConsumerAppInstalled() && !whiteListCheckUseCase.isWhatsAppSmbAppInstalled()) {
            return AddStickerPackActions.APPS_NOT_FOUND
        }
        val stickerPackWhitelistedInWhatsAppConsumer =
            whiteListCheckUseCase.isStickerPackWhitelistedInWhatsAppConsumer(identifier)
        val stickerPackWhitelistedInWhatsAppSmb = whiteListCheckUseCase.isStickerPackWhitelistedInWhatsAppSmb(identifier)

        return if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
            // ask users which app to add the pack to.
            AddStickerPackActions.ASK_USER_WHICH_APP
        } else if (!stickerPackWhitelistedInWhatsAppConsumer) {
            AddStickerPackActions.ADD_TO_CONSUMER
        } else if (!stickerPackWhitelistedInWhatsAppSmb) {
            AddStickerPackActions.ADD_TO_BUSINESS
        } else {
            AddStickerPackActions.PROMPT_UPDATE_CAUSE_FAILURE
        }
    }

    override fun stopAllBackgroundWork() {
        compositeDisposable.clear()
    }
}

enum class AddStickerPackActions {
    APPS_NOT_FOUND,
    ASK_USER_WHICH_APP,
    ADD_TO_CONSUMER,
    ADD_TO_BUSINESS,
    PROMPT_UPDATE_CAUSE_FAILURE
}