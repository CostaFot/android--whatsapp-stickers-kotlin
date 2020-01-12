package com.costafot.stickers.usecase

import android.content.ContentResolver
import java.io.ByteArrayOutputStream
import java.io.InputStream

class FetchStickerAssetUseCase(private val contentResolver: ContentResolver, private val uriResolverUseCase: UriResolverUseCase) {

    fun fetchStickerAsset(identifier: String, name: String): ByteArray {
        val inputStream: InputStream?
        try {
            inputStream = contentResolver.openInputStream(uriResolverUseCase.getStickerAssetUri(identifier, name))
        } catch (t: Throwable) {
            throw Exception("Could not open input stream for identifier $identifier, name $name")
        }

        val outputStream = ByteArrayOutputStream()

        try {
            inputStream.use {
                val bytes = ByteArray(it.available())
                outputStream.write(bytes, 0, it.read(bytes))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
            outputStream.close()
        }

        return outputStream.toByteArray()
    }
}
