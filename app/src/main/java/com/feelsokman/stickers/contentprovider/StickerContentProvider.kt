package com.feelsokman.stickers.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.feelsokman.stickers.R
import com.feelsokman.stickers.usecase.GetStringFromStorageUseCase
import dagger.android.AndroidInjection
import javax.inject.Inject

class StickerContentProvider : ContentProvider() {


    @Inject
    lateinit var getStringFromStorageUseCase: GetStringFromStorageUseCase

    private val uriMatcher by lazy {
        UriMatcher(UriMatcher.NO_MATCH)
    }

    override fun onCreate(): Boolean {
        AndroidInjection.inject(this)

        val authority: String = context!!.getString(R.string.content_provider_authority)
        if (!authority.startsWith(context!!.packageName)) {
            throw IllegalStateException("your authority (" + authority + ") for the content provider should start with your package name: " + context!!.packageName)
        }

        //the call to get the metadata for the sticker packs.
        uriMatcher.addURI(authority, METADATA, METADATA_CODE)

        //the call to get the metadata for single sticker pack. * represent the identifier
        uriMatcher.addURI(authority, "$METADATA/*", METADATA_CODE_FOR_SINGLE_PACK)

        //gets the list of stickers for a sticker pack, * respresent the identifier.
        uriMatcher.addURI(authority, "$STICKERS/*", STICKERS_CODE)

        for (stickerPack in getStickerPackList()) {
            uriMatcher.addURI(
                authority,
                STICKERS_ASSET + "/" + stickerPack.identifier + "/" + stickerPack.trayImageFile,
                STICKER_PACK_TRAY_ICON_CODE
            )
            for (sticker in stickerPack.getStickers()) {
                uriMatcher.addURI(
                    authority,
                    STICKERS_ASSET + "/" + stickerPack.identifier + "/" + sticker.imageFileName,
                    STICKERS_ASSET_CODE
                )
            }
        }

        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        //

        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        //
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        //
        return 10
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        //
        return 10
    }

    override fun getType(uri: Uri): String? {
        //

        return ""
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
}