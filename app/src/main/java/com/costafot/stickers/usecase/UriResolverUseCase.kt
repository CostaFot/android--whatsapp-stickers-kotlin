package com.costafot.stickers.usecase

import android.content.ContentResolver
import android.net.Uri
import com.costafot.stickers.contentprovider.METADATA
import com.costafot.stickers.contentprovider.STICKERS
import com.costafot.stickers.contentprovider.STICKERS_ASSET

class UriResolverUseCase(private val providerAuthority: String) {

    fun getStickerListUri(identifier: String): Uri =
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(providerAuthority)
            .appendPath(STICKERS)
            .appendPath(identifier)
            .build()

    fun getStickerAssetUri(identifier: String, stickerName: String): Uri =
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(providerAuthority)
            .appendPath(STICKERS_ASSET)
            .appendPath(identifier)
            .appendPath(stickerName)
            .build()

    fun getAuthorityUri(): Uri =
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(providerAuthority)
            .appendPath(METADATA)
            .build()
}