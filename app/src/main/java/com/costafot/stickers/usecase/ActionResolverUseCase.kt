package com.costafot.stickers.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ActionResolverUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val whiteListCheckUseCase: WhiteListCheckUseCase
) {

    suspend fun resolveActionAddStickerPackSuspended(identifier: String): AddStickerPackAction {
        return withContext(dispatcher) {
            resolve(identifier)
        }
    }

    private fun resolve(identifier: String): AddStickerPackAction {
        // if neither WhatsApp Consumer or WhatsApp Business is installed, then tell user to install the apps.
        if (!whiteListCheckUseCase.isWhatsAppConsumerAppInstalled() && !whiteListCheckUseCase.isWhatsAppSmbAppInstalled()) {
            return AddStickerPackAction.APPS_NOT_FOUND
        }
        val stickerPackWhitelistedInWhatsAppConsumer =
            whiteListCheckUseCase.isStickerPackWhitelistedInWhatsAppConsumer(identifier)
        val stickerPackWhitelistedInWhatsAppSmb = whiteListCheckUseCase.isStickerPackWhitelistedInWhatsAppSmb(identifier)

        return if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
            // ask users which app to add the pack to.
            AddStickerPackAction.ASK_USER_WHICH_APP
        } else if (!stickerPackWhitelistedInWhatsAppConsumer) {
            AddStickerPackAction.ADD_TO_CONSUMER
        } else if (!stickerPackWhitelistedInWhatsAppSmb) {
            AddStickerPackAction.ADD_TO_BUSINESS
        } else {
            AddStickerPackAction.PROMPT_UPDATE_CAUSE_FAILURE
        }
    }
}

enum class AddStickerPackAction {
    APPS_NOT_FOUND,
    ASK_USER_WHICH_APP,
    ADD_TO_CONSUMER,
    ADD_TO_BUSINESS,
    PROMPT_UPDATE_CAUSE_FAILURE
}