package com.feelsokman.stickers.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.text.TextUtils
import androidx.annotation.NonNull
import com.feelsokman.stickers.R
import com.feelsokman.stickers.contentprovider.model.Sticker
import com.feelsokman.stickers.contentprovider.model.StickerPack
import com.feelsokman.stickers.contentprovider.utils.ContentFileParser
import dagger.android.AndroidInjection
import timber.log.Timber
import java.io.IOException
import java.util.ArrayList
import java.util.Objects
import javax.inject.Inject

class StickerContentProvider : ContentProvider() {

    @Inject
    lateinit var contentFileParser: ContentFileParser

    private var stickerPackList: List<StickerPack>? = null

    private val uriMatcher by lazy {
        UriMatcher(UriMatcher.NO_MATCH)
    }

    override fun onCreate(): Boolean {
        AndroidInjection.inject(this)

        val authority: String = context!!.getString(R.string.content_provider_authority)
        if (!authority.startsWith(context!!.packageName)) {
            throw IllegalStateException("your authority (" + authority + ") for the content provider should start with your package name: " + context!!.packageName)
        }

        // the call to get the metadata for the sticker packs.
        uriMatcher.addURI(authority, METADATA, METADATA_CODE)

        // the call to get the metadata for single sticker pack. * represent the identifier
        uriMatcher.addURI(authority, "$METADATA/*", METADATA_CODE_FOR_SINGLE_PACK)

        // gets the list of stickers for a sticker pack, * respresent the identifier.
        uriMatcher.addURI(authority, "$STICKERS/*", STICKERS_CODE)

        for (stickerPack: StickerPack in getStickerPackList()) {
            uriMatcher.addURI(
                authority,
                STICKERS_ASSET + "/" + stickerPack.identifier + "/" + stickerPack.trayImageFile,
                STICKER_PACK_TRAY_ICON_CODE
            )
            for (sticker: Sticker in stickerPack.stickers!!) {
                uriMatcher.addURI(
                    authority,
                    STICKERS_ASSET + "/" + stickerPack.identifier + "/" + sticker.imageFileName,
                    STICKERS_ASSET_CODE
                )
            }
        }

        return true
    }

    private fun getStickerPackList(): List<StickerPack> {
        if (stickerPackList == null) {
            readContentFile(context!!)
        }
        return stickerPackList!!
    }

    @Synchronized
    private fun readContentFile(@NonNull context: Context) {
        try {
            context.assets.open(CONTENT_FILE_NAME)
                .use { contentsInputStream -> stickerPackList = contentFileParser.parseStickerPacks(contentsInputStream) }
        } catch (e: IOException) {
            throw RuntimeException(CONTENT_FILE_NAME + " file has some issues: " + e.message, e)
        } catch (e: IllegalStateException) {
            throw RuntimeException(CONTENT_FILE_NAME + " file has some issues: " + e.message, e)
        }
    }

    private fun getPackForAllStickerPacks(@NonNull uri: Uri): Cursor {
        return getStickerPackInfo(uri, getStickerPackList())
    }

    private fun getCursorForSingleStickerPack(@NonNull uri: Uri): Cursor {
        val identifier = uri.lastPathSegment
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                return getStickerPackInfo(uri, listOf(stickerPack))
            }
        }

        return getStickerPackInfo(uri, ArrayList())
    }

    @NonNull
    private fun getStickerPackInfo(@NonNull uri: Uri, @NonNull stickerPackList: List<StickerPack>): Cursor {
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
                LICENSE_AGREENMENT_WEBSITE
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
        cursor.setNotificationUri(Objects.requireNonNull(context).contentResolver, uri)
        return cursor
    }

    @NonNull
    private fun getStickersForAStickerPack(@NonNull uri: Uri): Cursor {
        val identifier = uri.lastPathSegment
        val cursor = MatrixCursor(arrayOf(STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY))

        for (stickerPack: StickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                for (sticker: Sticker in stickerPack.stickers!!) {
                    cursor.addRow(arrayOf<Any>(sticker.imageFileName!!, TextUtils.join(",", sticker.emojis!!)))
                }
            }
        }
        cursor.setNotificationUri(Objects.requireNonNull(context).contentResolver, uri)
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

    override fun getType(@NonNull uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            METADATA_CODE -> "vnd.android.cursor.dir/vnd." + context!!.getString(R.string.content_provider_authority) + "." + METADATA
            METADATA_CODE_FOR_SINGLE_PACK -> "vnd.android.cursor.item/vnd." + context!!.getString(R.string.content_provider_authority) + "." + METADATA
            STICKERS_CODE -> "vnd.android.cursor.dir/vnd." + context!!.getString(R.string.content_provider_authority) + "." + STICKERS
            STICKERS_ASSET_CODE -> "image/webp"
            STICKER_PACK_TRAY_ICON_CODE -> "image/png"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun openAssetFile(@NonNull uri: Uri, @NonNull mode: String): AssetFileDescriptor? {
        val matchCode = uriMatcher.match(uri)
        return if (matchCode == STICKERS_ASSET_CODE || matchCode == STICKER_PACK_TRAY_ICON_CODE) {
            getImageAsset(uri)
        } else null
    }

    @Throws(IllegalArgumentException::class)
    private fun getImageAsset(uri: Uri): AssetFileDescriptor? {
        val am = context!!.assets
        val pathSegments = uri.pathSegments
        if (pathSegments.size != 3) {
            throw IllegalArgumentException("path segments should be 3, uri is: $uri")
        }
        val fileName = pathSegments[pathSegments.size - 1]
        val identifier = pathSegments[pathSegments.size - 2]
        if (TextUtils.isEmpty(identifier)) {
            throw IllegalArgumentException("identifier is empty, uri: $uri")
        }
        if (TextUtils.isEmpty(fileName)) {
            throw IllegalArgumentException("file name is empty, uri: $uri")
        }
        // making sure the file that is trying to be fetched is in the list of stickers.
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                if (fileName == stickerPack.trayImageFile) {
                    return fetchFile(uri, am, fileName, identifier)
                } else {
                    for (sticker in stickerPack.stickers!!) {
                        if (fileName == sticker.imageFileName) {
                            return fetchFile(uri, am, fileName, identifier)
                        }
                    }
                }
            }
        }
        return null
    }

    private fun fetchFile(@NonNull uri: Uri, @NonNull am: AssetManager, @NonNull fileName: String, @NonNull identifier: String): AssetFileDescriptor? {
        return try {
            am.openFd("$identifier/$fileName")
        } catch (e: IOException) {
            Timber.e("IOException when getting asset file, uri:$uri", e)
            null
        }
    }

    companion object {
        const val STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier"
        const val STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name"
        const val STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher"
        const val STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon"
        const val ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link"
        const val IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link"
        const val PUBLISHER_EMAIL = "sticker_pack_publisher_email"
        const val PUBLISHER_WEBSITE = "sticker_pack_publisher_website"
        const val PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website"
        const val LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website"
        const val STICKER_FILE_NAME_IN_QUERY = "sticker_file_name"
        const val STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji"
        const val CONTENT_FILE_NAME = "contents.json"
        const val METADATA = "metadata"
        const val METADATA_CODE = 1
        const val METADATA_CODE_FOR_SINGLE_PACK = 2
        const val STICKERS = "stickers"
        const val STICKERS_CODE = 3
        const val STICKERS_ASSET = "stickers_asset"
        const val STICKERS_ASSET_CODE = 4
        const val STICKER_PACK_TRAY_ICON_CODE = 5
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