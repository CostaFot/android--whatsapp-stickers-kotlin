package com.feelsokman.stickers.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import com.feelsokman.stickers.contentprovider.model.Sticker
import com.feelsokman.stickers.contentprovider.model.StickerPack
import com.feelsokman.stickers.usecase.ContentFileParser
import dagger.android.AndroidInjection
import java.util.ArrayList
import javax.inject.Inject

class StickerContentProvider : ContentProvider() {

    @Inject
    lateinit var contentFileParser: ContentFileParser
    @Inject
    lateinit var assetManager: AssetManager
    @Inject
    lateinit var stickerProviderHelper: StickerProviderHelper
    @Inject
    lateinit var uriMatcher: UriMatcher

    private var stickerPackList: List<StickerPack> = mutableListOf()

    override fun onCreate(): Boolean {
        AndroidInjection.inject(this)

        // the call to get the metadata for the sticker packs.
        uriMatcher.addURI(stickerProviderHelper.providerAuthority, METADATA, METADATA_CODE)

        // the call to get the metadata for single sticker pack. * represent the identifier
        uriMatcher.addURI(stickerProviderHelper.providerAuthority, "$METADATA/*", METADATA_CODE_FOR_SINGLE_PACK)

        // gets the list of stickers for a sticker pack, * represent the identifier.
        uriMatcher.addURI(stickerProviderHelper.providerAuthority, "$STICKERS/*", STICKERS_CODE)

        for (stickerPack: StickerPack in getStickerPackList()) {
            uriMatcher.addURI(
                stickerProviderHelper.providerAuthority,
                STICKERS_ASSET + "/" + stickerPack.identifier + "/" + stickerPack.trayImageFile,
                STICKER_PACK_TRAY_ICON_CODE
            )
            val stickersList = stickerPack.stickers ?: throw Exception("Stick list in stickerpack is null")
            for (sticker: Sticker in stickersList) {
                uriMatcher.addURI(
                    stickerProviderHelper.providerAuthority,
                    STICKERS_ASSET + "/" + stickerPack.identifier + "/" + sticker.imageFileName,
                    STICKERS_ASSET_CODE
                )
            }
        }

        return true
    }

    private fun getStickerPackList(): List<StickerPack> {
        if (stickerPackList.isNullOrEmpty()) {
            readContentFile()
        }
        return stickerPackList
    }

    @Synchronized
    private fun readContentFile() {
        try {
            assetManager.open(CONTENT_FILE_NAME).use { contentsInputStream ->
                stickerPackList = contentFileParser.parseStickerPacks(contentsInputStream)
            }
        } catch (e: Throwable) {
            throw Exception(CONTENT_FILE_NAME + " file has some issues: ${e.localizedMessage}")
        }
    }

    private fun getPackForAllStickerPacks(uri: Uri): Cursor {
        return getStickerPackInfo(uri, getStickerPackList())
    }

    private fun getCursorForSingleStickerPack(uri: Uri): Cursor {
        val identifier = uri.lastPathSegment
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                return getStickerPackInfo(uri, listOf(stickerPack))
            }
        }

        return getStickerPackInfo(uri, ArrayList())
    }

    @NonNull
    private fun getStickerPackInfo(uri: Uri, stickerPackList: List<StickerPack>): Cursor {
        val cursor = MatrixCursor(
            arrayOf(
                STICKER_PACK_IDENTIFIER_IN_QUERY,
                STICKER_PACK_NAME_IN_QUERY,
                STICKER_PACK_PUBLISHER_IN_QUERY,
                STICKER_PACK_ICON_IN_QUERY,
                ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                PUBLISHER_EMAIL,
                PUBLISHER_WEBSITE,
                PRIVACY_POLICY_WEBSITE,
                LICENSE_AGREEMENT_WEBSITE
            )
        )
        for (stickerPack in stickerPackList) {
            val builder = cursor.newRow()
            builder.add(stickerPack.identifier)
            builder.add(stickerPack.name)
            builder.add(stickerPack.publisher)
            builder.add(stickerPack.trayImageFile)
            builder.add(stickerPack.androidPlayStoreLink)
            builder.add(stickerPack.iosAppStoreLink)
            builder.add(stickerPack.publisherEmail)
            builder.add(stickerPack.publisherWebsite)
            builder.add(stickerPack.privacyPolicyWebsite)
            builder.add(stickerPack.licenseAgreementWebsite)
        }
        cursor.setNotificationUri(stickerProviderHelper.contentResolver, uri)
        return cursor
    }

    @NonNull
    private fun getStickersForAStickerPack(uri: Uri): Cursor {
        val identifier = uri.lastPathSegment
        val cursor = MatrixCursor(arrayOf(STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY))

        for (stickerPack: StickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                val stickersList = stickerPack.stickers ?: throw Exception("Stick list in stickerpack is null")
                for (sticker: Sticker in stickersList) {
                    val imageFileName = sticker.imageFileName ?: throw Exception("ImageFileName is null")
                    val emojis = sticker.emojis ?: throw Exception("Emojis are null")
                    cursor.addRow(arrayOf<Any>(imageFileName, TextUtils.join(",", emojis)))
                }
            }
        }
        cursor.setNotificationUri(stickerProviderHelper.contentResolver, uri)
        return cursor
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            METADATA_CODE -> getPackForAllStickerPacks(uri)
            METADATA_CODE_FOR_SINGLE_PACK -> getCursorForSingleStickerPack(uri)
            STICKERS_CODE -> getStickersForAStickerPack(uri)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            METADATA_CODE -> "vnd.android.cursor.dir/vnd." + stickerProviderHelper.providerAuthority + "." + METADATA
            METADATA_CODE_FOR_SINGLE_PACK -> "vnd.android.cursor.item/vnd." + stickerProviderHelper.providerAuthority + "." + METADATA
            STICKERS_CODE -> "vnd.android.cursor.dir/vnd." + stickerProviderHelper.providerAuthority + "." + STICKERS
            STICKERS_ASSET_CODE -> "image/webp"
            STICKER_PACK_TRAY_ICON_CODE -> "image/png"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        val matchCode = uriMatcher.match(uri)
        return if (matchCode == STICKERS_ASSET_CODE || matchCode == STICKER_PACK_TRAY_ICON_CODE) {
            getImageAsset(uri)
        } else null
    }

    @Throws(IllegalArgumentException::class)
    private fun getImageAsset(uri: Uri): AssetFileDescriptor? {
        val pathSegments = uri.pathSegments
        if (pathSegments.size != 3) {
            throw IllegalArgumentException("path segments should be 3, uri is: $uri")
        }
        val fileName = pathSegments[pathSegments.size - 1]
        val identifier = pathSegments[pathSegments.size - 2]
        if (identifier.isBlank()) {
            throw IllegalArgumentException("identifier is empty, uri: $uri")
        }
        if (fileName.isBlank()) {
            throw IllegalArgumentException("file name is empty, uri: $uri")
        }
        // making sure the file that is trying to be fetched is in the list of stickers.
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                if (fileName == stickerPack.trayImageFile) {
                    return fetchFile(uri, assetManager, fileName, identifier)
                } else {
                    val stickerList = stickerPack.stickers ?: throw Exception("stickerList is null")
                    for (sticker in stickerList) {
                        if (fileName == sticker.imageFileName) {
                            return fetchFile(uri, assetManager, fileName, identifier)
                        }
                    }
                }
            }
        }
        return null
    }

    private fun fetchFile(uri: Uri, assetManager: AssetManager, fileName: String, identifier: String): AssetFileDescriptor? {
        return try {
            assetManager.openFd("$identifier/$fileName")
        } catch (e: Throwable) {
            Log.e(TAG, "IOException when getting asset file, uri:$uri, error: ${e.localizedMessage}")
            null
        }
    }

    companion object {
        const val TAG = "StickerContentProvider"
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not supported")
    }
}