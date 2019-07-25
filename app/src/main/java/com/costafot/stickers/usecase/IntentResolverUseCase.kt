package com.costafot.stickers.usecase

import android.content.Intent

class IntentResolverUseCase(val providerAuthority: String) {

    fun createChooserIntentToAddStickerPack(identifier: String, stickerPackName: String): Intent {
        return Intent().apply {
            action = ACTION_ENABLE_STICKER_PACK
            putExtra(EXTRA_STICKER_PACK_ID, identifier)
            putExtra(EXTRA_STICKER_PACK_AUTHORITY, providerAuthority)
            putExtra(EXTRA_STICKER_PACK_NAME, stickerPackName)
        }
    }

    fun createConsumerIntentToAddStickerPack(identifier: String, stickerPackName: String): Intent {
        return Intent().apply {
            action = ACTION_ENABLE_STICKER_PACK
            putExtra(EXTRA_STICKER_PACK_ID, identifier)
            putExtra(EXTRA_STICKER_PACK_AUTHORITY, providerAuthority)
            putExtra(EXTRA_STICKER_PACK_NAME, stickerPackName)

            setPackage(WhiteListCheckUseCase.CONSUMER_WHATSAPP_PACKAGE_NAME)
        }
    }

    fun createBusinessIntentToAddStickerPack(identifier: String, stickerPackName: String): Intent {
        return Intent().apply {
            action = ACTION_ENABLE_STICKER_PACK
            putExtra(EXTRA_STICKER_PACK_ID, identifier)
            putExtra(EXTRA_STICKER_PACK_AUTHORITY, providerAuthority)
            putExtra(EXTRA_STICKER_PACK_NAME, stickerPackName)

            setPackage(WhiteListCheckUseCase.SMB_WHATSAPP_PACKAGE_NAME)
        }
    }

    companion object {
        private const val ACTION_ENABLE_STICKER_PACK = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        private const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
        private const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
        private const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"
    }
}