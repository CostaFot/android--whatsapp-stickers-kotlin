package com.feelsokman.stickers.usecase

import android.content.ContentResolver
import android.content.res.Resources
import android.net.Uri
import com.feelsokman.stickers.R
import com.feelsokman.stickers.contentprovider.StickerContentProvider

class UriResolverUseCase(private val resources: Resources) {

    fun getStickerListUri(identifier: String): Uri {
        return Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(resources.getString(R.string.content_provider_authority))
            .appendPath(StickerContentProvider.STICKERS).appendPath(identifier).build()
    }

    fun getStickerAssetUri(identifier: String, stickerName: String): Uri {
        return Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(resources.getString(R.string.content_provider_authority))
            .appendPath(StickerContentProvider.STICKERS_ASSET).appendPath(identifier).appendPath(stickerName).build()
    }

    fun getAuthorityUri(): Uri {
        return Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(resources.getString(R.string.content_provider_authority))
            .appendPath(StickerContentProvider.METADATA).build()
    }
}