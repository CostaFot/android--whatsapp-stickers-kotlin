package com.costafot.stickers.usecase

import android.content.ContentResolver
import android.database.Cursor
import com.costafot.stickers.contentprovider.ANDROID_APP_DOWNLOAD_LINK_IN_QUERY
import com.costafot.stickers.contentprovider.IOS_APP_DOWNLOAD_LINK_IN_QUERY
import com.costafot.stickers.contentprovider.LICENSE_AGREEMENT_WEBSITE
import com.costafot.stickers.contentprovider.PRIVACY_POLICY_WEBSITE
import com.costafot.stickers.contentprovider.PUBLISHER_EMAIL
import com.costafot.stickers.contentprovider.PUBLISHER_WEBSITE
import com.costafot.stickers.contentprovider.STICKER_FILE_EMOJI_IN_QUERY
import com.costafot.stickers.contentprovider.STICKER_FILE_NAME_IN_QUERY
import com.costafot.stickers.contentprovider.STICKER_PACK_ICON_IN_QUERY
import com.costafot.stickers.contentprovider.STICKER_PACK_IDENTIFIER_IN_QUERY
import com.costafot.stickers.contentprovider.STICKER_PACK_NAME_IN_QUERY
import com.costafot.stickers.contentprovider.STICKER_PACK_PUBLISHER_IN_QUERY
import com.costafot.stickers.contentprovider.StickerProviderHelper
import com.costafot.stickers.contentprovider.model.Sticker
import com.costafot.stickers.contentprovider.model.StickerPack
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.ArrayList
import java.util.HashSet

/**
 * Main class loading all sticker packs into the app.
 */
class StickerPackLoaderUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val stickerProviderHelper: StickerProviderHelper,
    private val fetchStickerAssetUseCase: FetchStickerAssetUseCase,
    private val uriResolverUseCase: UriResolverUseCase,
    private val stickerPackValidator: StickerPackValidator,
    private val whiteListCheckUseCase: WhiteListCheckUseCase
) {

    suspend fun loadStickerPacksSuspended(): ArrayList<StickerPack> {
        return withContext(dispatcher) {
            fetchStickerPacks()
        }
    }

    private fun fetchStickerPacks(): ArrayList<StickerPack> {
        val stickerPackList: ArrayList<StickerPack> = arrayListOf()
        try {
            stickerProviderHelper.contentResolver.query(uriResolverUseCase.getAuthorityUri(), null, null, null, null)?.use {
                stickerPackList.addAll(fetchFromContentProvider(it))
            }
        } catch (e: Exception) {
            throw Exception("Could not fetch from content provider $stickerProviderHelper.providerAuthority, error ${e.localizedMessage}")
        }

        checkUniqueIdentifiers(stickerPackList)

        if (stickerPackList.isEmpty()) {
            throw IllegalStateException("There should be at least one sticker pack in the app")
        }
        for (stickerPack in stickerPackList) {
            val stickers = getStickersForPack(stickerPack)
            stickerPack.stickers = stickers
            stickerPackValidator.verifyStickerPackValidity(stickerPack)
        }

        for (stickerPack in stickerPackList) {
            stickerPack.isWhitelisted = whiteListCheckUseCase.isWhitelisted(stickerPack.identifier!!)
        }
        return stickerPackList
    }

    private fun checkUniqueIdentifiers(stickerPackList: ArrayList<StickerPack>) {
        val identifierSet = HashSet<String>()
        for (stickerPack in stickerPackList) {
            if (identifierSet.contains(stickerPack.identifier)) {
                throw IllegalStateException("sticker pack identifiers should be unique, there are more than one pack with identifier:" + stickerPack.identifier)
            } else {
                identifierSet.add(stickerPack.identifier!!)
            }
        }
    }

    private fun getStickersForPack(stickerPack: StickerPack): List<Sticker> {
        val stickers = fetchFromContentProviderForStickers(stickerPack.identifier!!, stickerProviderHelper.contentResolver)
        for (sticker in stickers) {
            try {
                val bytes: ByteArray =
                    fetchStickerAssetUseCase.fetchStickerAsset(stickerPack.identifier, sticker.imageFileName!!)
                if (bytes.isEmpty()) {
                    throw Exception("Asset file is empty, pack: " + stickerPack.name + ", sticker: " + sticker.imageFileName)
                }
                sticker.size = bytes.size.toLong()
            } catch (e: Throwable) {
                throw Exception("Asset file doesn't exist. pack: $stickerPack.name, sticker: $sticker.imageFileName, error: ${e.localizedMessage}")
            }
        }
        return stickers
    }

    private fun fetchFromContentProvider(cursor: Cursor): ArrayList<StickerPack> {
        val stickerPackList = ArrayList<StickerPack>()
        cursor.moveToFirst()
        do {
            val identifier = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_PACK_IDENTIFIER_IN_QUERY))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_PACK_NAME_IN_QUERY))
            val publisher = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_PACK_PUBLISHER_IN_QUERY))
            val trayImage = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_PACK_ICON_IN_QUERY))
            val androidPlayStoreLink = cursor.getString(cursor.getColumnIndexOrThrow(ANDROID_APP_DOWNLOAD_LINK_IN_QUERY))
            val iosAppLink = cursor.getString(cursor.getColumnIndexOrThrow(IOS_APP_DOWNLOAD_LINK_IN_QUERY))
            val publisherEmail = cursor.getString(cursor.getColumnIndexOrThrow(PUBLISHER_EMAIL))
            val publisherWebsite = cursor.getString(cursor.getColumnIndexOrThrow(PUBLISHER_WEBSITE))
            val privacyPolicyWebsite = cursor.getString(cursor.getColumnIndexOrThrow(PRIVACY_POLICY_WEBSITE))
            val licenseAgreementWebsite = cursor.getString(cursor.getColumnIndexOrThrow(LICENSE_AGREEMENT_WEBSITE))
            val stickerPack = StickerPack(
                identifier = identifier,
                name = name,
                publisher = publisher,
                trayImageFile = trayImage,
                publisherEmail = publisherEmail,
                publisherWebsite = publisherWebsite,
                privacyPolicyWebsite = privacyPolicyWebsite,
                licenseAgreementWebsite = licenseAgreementWebsite,
                androidPlayStoreLink = androidPlayStoreLink,
                iosAppStoreLink = iosAppLink
            )
            stickerPackList.add(stickerPack)
        } while (cursor.moveToNext())

        cursor.close()
        return stickerPackList
    }

    private fun fetchFromContentProviderForStickers(identifier: String, contentResolver: ContentResolver): List<Sticker> {
        val uri = uriResolverUseCase.getStickerListUri(identifier)

        val projection = arrayOf(STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val stickerList = ArrayList<Sticker>()

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_FILE_NAME_IN_QUERY))
                val emojisConcatenated = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_FILE_EMOJI_IN_QUERY))
                stickerList.add(
                    Sticker(name, listOf(*emojisConcatenated.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
                )
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return stickerList
    }
}