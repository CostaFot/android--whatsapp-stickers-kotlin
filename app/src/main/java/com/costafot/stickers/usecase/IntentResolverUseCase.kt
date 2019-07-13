package com.costafot.stickers.usecase

import android.content.Intent
import com.costafot.stickers.ui.activity.MainActivity

class IntentResolverUseCase(val providerAuthority: String) {

    fun createIntentToAddStickerPack(identifier: String, stickerPackName: String): Intent {
        return Intent().apply {
            action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
            putExtra(MainActivity.EXTRA_STICKER_PACK_ID, identifier)
            putExtra(MainActivity.EXTRA_STICKER_PACK_AUTHORITY, providerAuthority)
            putExtra(MainActivity.EXTRA_STICKER_PACK_NAME, stickerPackName)
        }
    }
}