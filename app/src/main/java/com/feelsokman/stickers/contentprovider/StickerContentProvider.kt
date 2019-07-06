package com.feelsokman.stickers.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.feelsokman.stickers.usecase.GetStringFromStorageUseCase
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class StickerContentProvider : ContentProvider() {

    @Inject
    lateinit var getStringFromStorageUseCase: GetStringFromStorageUseCase

    override fun onCreate(): Boolean {
        AndroidInjection.inject(this)
        Timber.d("STARTING ON CREATE")

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
}