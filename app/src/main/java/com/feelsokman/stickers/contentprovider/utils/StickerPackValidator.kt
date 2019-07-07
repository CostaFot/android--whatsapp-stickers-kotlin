package com.feelsokman.stickers.contentprovider.utils

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import com.facebook.animated.webp.WebPImage
import com.feelsokman.stickers.contentprovider.model.Sticker
import com.feelsokman.stickers.contentprovider.model.StickerPack
import com.feelsokman.stickers.usecase.FetchStickerAssetUseCase
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class StickerPackValidator(private val fetchStickerAssetUseCase: FetchStickerAssetUseCase) {
    companion object {
        private const val STICKER_FILE_SIZE_LIMIT_KB = 100
        private const val EMOJI_LIMIT = 3
        private const val IMAGE_HEIGHT = 512
        private const val IMAGE_WIDTH = 512
        private const val STICKER_SIZE_MIN = 3
        private const val STICKER_SIZE_MAX = 30
        private const val CHAR_COUNT_MAX = 128
        private const val ONE_KIBIBYTE = (8 * 1024).toLong()
        private const val TRAY_IMAGE_FILE_SIZE_MAX_KB = 50
        private const val TRAY_IMAGE_DIMENSION_MIN = 24
        private const val TRAY_IMAGE_DIMENSION_MAX = 512
        private const val PLAY_STORE_DOMAIN = "play.google.com"
        private const val APPLE_STORE_DOMAIN = "itunes.apple.com"
    }

    /**
     * Checks whether a sticker pack contains valid data
     */
    @Throws(IllegalStateException::class)
    fun verifyStickerPackValidity(stickerPack: StickerPack) {
        if (TextUtils.isEmpty(stickerPack.identifier)) {
            throw IllegalStateException("sticker pack identifier is empty")
        }
        if (stickerPack.identifier!!.length > CHAR_COUNT_MAX) {
            throw IllegalStateException("sticker pack identifier cannot exceed $CHAR_COUNT_MAX characters")
        }
        checkStringValidity(stickerPack.identifier!!)
        if (TextUtils.isEmpty(stickerPack.publisher)) {
            throw IllegalStateException("sticker pack publisher is empty, sticker pack identifier:" + stickerPack.identifier)
        }
        if (stickerPack.publisher!!.length > CHAR_COUNT_MAX) {
            throw IllegalStateException("sticker pack publisher cannot exceed " + CHAR_COUNT_MAX + " characters, sticker pack identifier:" + stickerPack.identifier)
        }
        if (TextUtils.isEmpty(stickerPack.name)) {
            throw IllegalStateException("sticker pack name is empty, sticker pack identifier:" + stickerPack.identifier)
        }
        if (stickerPack.name!!.length > CHAR_COUNT_MAX) {
            throw IllegalStateException("sticker pack name cannot exceed " + CHAR_COUNT_MAX + " characters, sticker pack identifier:" + stickerPack.identifier)
        }
        if (TextUtils.isEmpty(stickerPack.trayImageFile)) {
            throw IllegalStateException("sticker pack tray id is empty, sticker pack identifier:" + stickerPack.identifier)
        }
        if (!TextUtils.isEmpty(stickerPack.androidPlayStoreLink) && !isValidWebsiteUrl(stickerPack.androidPlayStoreLink!!)) {
            throw IllegalStateException("Make sure to include http or https in url links, android play store link is not a valid url: " + stickerPack.androidPlayStoreLink)
        }
        if (!TextUtils.isEmpty(stickerPack.androidPlayStoreLink) && !isURLInCorrectDomain(
                stickerPack.androidPlayStoreLink!!,
                PLAY_STORE_DOMAIN
            )
        ) {
            throw IllegalStateException("android play store link should use play store domain: $PLAY_STORE_DOMAIN")
        }
        if (!TextUtils.isEmpty(stickerPack.iosAppStoreLink) && !isValidWebsiteUrl(stickerPack.iosAppStoreLink!!)) {
            throw IllegalStateException("Make sure to include http or https in url links, ios app store link is not a valid url: " + stickerPack.iosAppStoreLink)
        }
        if (!TextUtils.isEmpty(stickerPack.iosAppStoreLink) && !isURLInCorrectDomain(
                stickerPack.iosAppStoreLink!!,
                APPLE_STORE_DOMAIN
            )
        ) {
            throw IllegalStateException("iOS app store link should use app store domain: $APPLE_STORE_DOMAIN")
        }
        if (!TextUtils.isEmpty(stickerPack.licenseAgreementWebsite) && !isValidWebsiteUrl(stickerPack.licenseAgreementWebsite!!)) {
            throw IllegalStateException("Make sure to include http or https in url links, license agreement link is not a valid url: " + stickerPack.licenseAgreementWebsite)
        }
        if (!TextUtils.isEmpty(stickerPack.privacyPolicyWebsite) && !isValidWebsiteUrl(stickerPack.privacyPolicyWebsite!!)) {
            throw IllegalStateException("Make sure to include http or https in url links, privacy policy link is not a valid url: " + stickerPack.privacyPolicyWebsite)
        }
        if (!TextUtils.isEmpty(stickerPack.publisherWebsite) && !isValidWebsiteUrl(stickerPack.publisherWebsite!!)) {
            throw IllegalStateException("Make sure to include http or https in url links, publisher website link is not a valid url: " + stickerPack.publisherWebsite)
        }
        if (!TextUtils.isEmpty(stickerPack.publisherEmail) && !Patterns.EMAIL_ADDRESS.matcher(stickerPack.publisherEmail).matches()) {
            throw IllegalStateException("publisher email does not seem valid, email is: " + stickerPack.publisherEmail)
        }
        try {
            val bytes =
                fetchStickerAssetUseCase.fetchStickerAsset(stickerPack.identifier!!, stickerPack.trayImageFile!!)
            if (bytes.size > TRAY_IMAGE_FILE_SIZE_MAX_KB * ONE_KIBIBYTE) {
                throw IllegalStateException("tray image should be less than " + TRAY_IMAGE_FILE_SIZE_MAX_KB + " KB, tray image file: " + stickerPack.trayImageFile)
            }
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            if (bitmap.height > TRAY_IMAGE_DIMENSION_MAX || bitmap.height < TRAY_IMAGE_DIMENSION_MIN) {
                throw IllegalStateException("tray image height should between " + TRAY_IMAGE_DIMENSION_MIN + " and " + TRAY_IMAGE_DIMENSION_MAX + " pixels, current tray image height is " + bitmap.height + ", tray image file: " + stickerPack.trayImageFile)
            }
            if (bitmap.width > TRAY_IMAGE_DIMENSION_MAX || bitmap.width < TRAY_IMAGE_DIMENSION_MIN) {
                throw IllegalStateException("tray image width should be between " + TRAY_IMAGE_DIMENSION_MIN + " and " + TRAY_IMAGE_DIMENSION_MAX + " pixels, current tray image width is " + bitmap.width + ", tray image file: " + stickerPack.trayImageFile)
            }
        } catch (e: IOException) {
            throw IllegalStateException("Cannot open tray image, " + stickerPack.trayImageFile, e)
        }

        val stickers = stickerPack.stickers
        if (stickers!!.size < STICKER_SIZE_MIN || stickers.size > STICKER_SIZE_MAX) {
            throw IllegalStateException("sticker pack sticker count should be between 3 to 30 inclusive, it currently has " + stickers.size + ", sticker pack identifier:" + stickerPack.identifier)
        }
        for (sticker in stickers) {
            validateSticker(stickerPack.identifier!!, sticker)
        }
    }

    @Throws(IllegalStateException::class)
    private fun validateSticker(identifier: String, sticker: Sticker) {
        if (sticker.emojis!!.size > EMOJI_LIMIT) {
            throw IllegalStateException("emoji count exceed limit, sticker pack identifier:" + identifier + ", filename:" + sticker.imageFileName)
        }
        if (TextUtils.isEmpty(sticker.imageFileName)) {
            throw IllegalStateException("no file path for sticker, sticker pack identifier:$identifier")
        }
        validateStickerFile(identifier, sticker.imageFileName!!)
    }

    @Throws(IllegalStateException::class)
    private fun validateStickerFile(identifier: String, fileName: String) {
        try {
            val bytes = fetchStickerAssetUseCase.fetchStickerAsset(identifier, fileName)
            if (bytes.size > STICKER_FILE_SIZE_LIMIT_KB * ONE_KIBIBYTE) {
                throw IllegalStateException("sticker should be less than " + STICKER_FILE_SIZE_LIMIT_KB + "KB, sticker pack identifier:" + identifier + ", filename:" + fileName)
            }
            try {
                val webPImage = WebPImage.create(bytes)
                if (webPImage.height != IMAGE_HEIGHT) {
                    throw IllegalStateException("sticker height should be $IMAGE_HEIGHT, sticker pack identifier:$identifier, filename:$fileName")
                }
                if (webPImage.height != IMAGE_WIDTH) {
                    throw IllegalStateException("sticker width should be $IMAGE_WIDTH, sticker pack identifier:$identifier, filename:$fileName")
                }
                if (webPImage.frameCount > 1) {
                    throw IllegalStateException("sticker should be a static image, no animated sticker support at the moment, sticker pack identifier:$identifier, filename:$fileName")
                }
            } catch (e: IllegalArgumentException) {
                throw IllegalStateException(
                    "Error parsing webp image, sticker pack identifier:$identifier, filename:$fileName",
                    e
                )
            }
        } catch (e: IOException) {
            throw IllegalStateException("cannot open sticker file: sticker pack identifier:$identifier, filename:$fileName", e)
        }
    }

    private fun checkStringValidity(string: String) {
        val pattern = "[\\w-.,'\\s]+" // [a-zA-Z0-9_-.' ]
        if (!string.matches(pattern.toRegex())) {
            throw IllegalStateException("$string contains invalid characters, allowed characters are a to z, A to Z, _ , ' - . and space character")
        }
        if (string.contains("..")) {
            throw IllegalStateException("$string cannot contain ..")
        }
    }

    @Throws(IllegalStateException::class)
    private fun isValidWebsiteUrl(websiteUrl: String): Boolean {
        try {
            URL(websiteUrl)
        } catch (e: MalformedURLException) {
            Log.e("StickerPackValidator", "url: $websiteUrl is malformed")
            throw IllegalStateException("url: $websiteUrl is malformed", e)
        }

        return URLUtil.isHttpUrl(websiteUrl) || URLUtil.isHttpsUrl(websiteUrl)
    }

    @Throws(IllegalStateException::class)
    private fun isURLInCorrectDomain(urlString: String, domain: String): Boolean {
        try {
            val url = URL(urlString)
            if (domain == url.host) {
                return true
            }
        } catch (e: MalformedURLException) {
            Log.e("StickerPackValidator", "url: $urlString is malformed")
            throw IllegalStateException("url: $urlString is malformed")
        }

        return false
    }
}