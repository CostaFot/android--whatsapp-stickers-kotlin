package com.costafot.stickers.usecase

import com.costafot.stickers.coroutine.DispatcherProvider
import kotlinx.coroutines.withContext

class ActionResolverUseCase(
    private val dispatchers: DispatcherProvider,
    private val whiteListCheckUseCase: WhiteListCheckUseCase
) {

    suspend fun resolveActionAddStickerPack(identifier: String): AddStickerPackAction {
        return withContext(dispatchers.io) {
            resolve(identifier)
        }
    }

    private suspend fun resolve(identifier: String): AddStickerPackAction = withContext(dispatchers.io) {
        // if neither WhatsApp Consumer or WhatsApp Business is installed, then tell user to install the apps.
        if (!whiteListCheckUseCase.isWhatsAppConsumerAppInstalled() && !whiteListCheckUseCase.isWhatsAppSmbAppInstalled()) {
            return@withContext AddStickerPackAction.APPS_NOT_FOUND
        } else {
            val stickerPackWhitelistedInWhatsAppConsumer =
                whiteListCheckUseCase.isStickerPackWhitelistedInWhatsAppConsumer(identifier)
            val stickerPackWhitelistedInWhatsAppSmb = whiteListCheckUseCase.isStickerPackWhitelistedInWhatsAppSmb(identifier)

            when {
                !stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb -> {
                    // ask users which app to add the pack to.
                    return@withContext AddStickerPackAction.ASK_USER_WHICH_APP
                }
                !stickerPackWhitelistedInWhatsAppConsumer -> {
                    return@withContext AddStickerPackAction.ADD_TO_CONSUMER
                }
                !stickerPackWhitelistedInWhatsAppSmb -> {
                    return@withContext AddStickerPackAction.ADD_TO_BUSINESS
                }
                else -> {
                    return@withContext AddStickerPackAction.PROMPT_UPDATE_CAUSE_FAILURE
                }
            }
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
