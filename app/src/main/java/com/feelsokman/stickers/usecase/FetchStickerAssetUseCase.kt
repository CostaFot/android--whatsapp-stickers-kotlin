package com.feelsokman.stickers.usecase

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.IOException

class FetchStickerAssetUseCase(private val context: Context, private val uriResolverUseCase: UriResolverUseCase) {

    @Throws(IOException::class)
    fun fetchStickerAsset(identifier: String, name: String): ByteArray {
        context.contentResolver.openInputStream(uriResolverUseCase.getStickerAssetUri(identifier, name))!!.use { inputStream ->
            ByteArrayOutputStream().use { buffer ->
                val bytes = ByteArray(16384)

                buffer.write(bytes, 0, inputStream.read(bytes))

                /*while ((read = inputStream.read(data, 0, data.size)) != -1) {
                    buffer.write(data, 0, read)
                }*/
                return buffer.toByteArray()
            }
        }
    }
}