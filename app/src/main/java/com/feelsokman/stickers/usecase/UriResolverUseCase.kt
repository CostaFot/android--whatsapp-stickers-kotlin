package com.feelsokman.stickers.usecase

import android.content.ContentResolver
import android.net.Uri
import com.feelsokman.stickers.contentprovider.StickerContentProvider

class UriResolverUseCase(private val providerAuthority: String) {

    fun getStickerListUri(identifier: String): Uri {
        return Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(providerAuthority)
            .appendPath(StickerContentProvider.STICKERS).appendPath(identifier).build()
    }

    fun getStickerAssetUri(identifier: String, stickerName: String): Uri {
        return Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(providerAuthority)
            .appendPath(StickerContentProvider.STICKERS_ASSET).appendPath(identifier).appendPath(stickerName).build()
    }

    fun getAuthorityUri(): Uri {
        return Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(providerAuthority)
            .appendPath(StickerContentProvider.METADATA).build()
    }
}