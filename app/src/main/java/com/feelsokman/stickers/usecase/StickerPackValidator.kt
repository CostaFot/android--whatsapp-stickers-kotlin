package com.feelsokman.stickers.usecase

import android.graphics.BitmapFactory
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import com.facebook.animated.webp.WebPImage
import com.feelsokman.stickers.contentprovider.model.Sticker
import com.feelsokman.stickers.contentprovider.model.StickerPack
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
    @Throws(Throwable::class)
    fun verifyStickerPackValidity(stickerPack: StickerPack) {
        val identifier = stickerPack.identifier
        val publisher = stickerPack.publisher
        val name = stickerPack.name
        val trayImageFile = stickerPack.trayImageFile
        val androidPlayStoreLink = stickerPack.androidPlayStoreLink
        val iosAppStoreLink = stickerPack.iosAppStoreLink
        val licenseAgreementWebsite = stickerPack.licenseAgreementWebsite
        val privacyPolicyWebsite = stickerPack.privacyPolicyWebsite
        val publisherWebsite = stickerPack.publisherWebsite
        val publisherEmail = stickerPack.publisherEmail

        if (identifier.isNullOrBlank()) {
            throw IllegalStateException("sticker pack identifier is empty")
        }
        if (identifier.length > CHAR_COUNT_MAX) {
            throw IllegalStateException("sticker pack identifier cannot exceed $CHAR_COUNT_MAX characters")
        }
        checkStringValidity(identifier)
        if (publisher.isNullOrBlank()) {
            throw IllegalStateException("sticker pack publisher is empty, sticker pack identifier: $identifier")
        }
        if (publisher.length > CHAR_COUNT_MAX) {
            throw IllegalStateException("sticker pack publisher cannot exceed $CHAR_COUNT_MAX characters, sticker pack identifier: $identifier")
        }
        if (name.isNullOrBlank()) {
            throw IllegalStateException("sticker pack name is empty, sticker pack identifier:" + stickerPack.identifier)
        }
        if (name.length > CHAR_COUNT_MAX) {
            throw IllegalStateException("sticker pack name cannot exceed $CHAR_COUNT_MAX characters, sticker pack identifier: $identifier")
        }
        if (trayImageFile.isNullOrBlank()) {
            throw IllegalStateException("sticker pack tray id is empty, sticker pack identifier: $identifier")
        }
        if (!androidPlayStoreLink.isNullOrBlank() && !isValidWebsiteUrl(androidPlayStoreLink)) {
            throw IllegalStateException("Make sure to include http or https in url links, android play store link is not a valid url: $androidPlayStoreLink")
        }
        if (!androidPlayStoreLink.isNullOrBlank() && !isURLInCorrectDomain(
                androidPlayStoreLink,
                PLAY_STORE_DOMAIN
            )
        ) {
            throw IllegalStateException("android play store link should use play store domain: $PLAY_STORE_DOMAIN")
        }
        if (!iosAppStoreLink.isNullOrBlank() && !isValidWebsiteUrl(iosAppStoreLink)) {
            throw IllegalStateException("Make sure to include http or https in url links, ios app store link is not a valid url: " + stickerPack.iosAppStoreLink)
        }
        if (!iosAppStoreLink.isNullOrBlank() && !isURLInCorrectDomain(
                stickerPack.iosAppStoreLink!!,
                APPLE_STORE_DOMAIN
            )
        ) {
            throw IllegalStateException("iOS app store link should use app store domain: $APPLE_STORE_DOMAIN")
        }
        if (!licenseAgreementWebsite.isNullOrBlank() && !isValidWebsiteUrl(licenseAgreementWebsite)) {
            throw IllegalStateException("Make sure to include http or https in url links, license agreement link is not a valid url: $licenseAgreementWebsite")
        }
        if (!privacyPolicyWebsite.isNullOrBlank() && !isValidWebsiteUrl(privacyPolicyWebsite)) {
            throw IllegalStateException("Make sure to include http or https in url links, privacy policy link is not a valid url: $privacyPolicyWebsite")
        }
        if (!publisherWebsite.isNullOrBlank() && !isValidWebsiteUrl(publisherWebsite)) {
            throw IllegalStateException("Make sure to include http or https in url links, publisher website link is not a valid url: " + stickerPack.publisherWebsite)
        }
        if (!publisherEmail.isNullOrBlank() && !Patterns.EMAIL_ADDRESS.matcher(publisherEmail).matches()) {
            throw IllegalStateException("publisher email does not seem valid, email is: $publisherEmail")
        }
        try {
            val bytes = fetchStickerAssetUseCase.fetchStickerAsset(identifier, trayImageFile)
            if (bytes.size > TRAY_IMAGE_FILE_SIZE_MAX_KB * ONE_KIBIBYTE) {
                throw IllegalStateException("tray image should be less than $TRAY_IMAGE_FILE_SIZE_MAX_KB KB, tray image file: $trayImageFile")
            }
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            if (bitmap.height > TRAY_IMAGE_DIMENSION_MAX || bitmap.height < TRAY_IMAGE_DIMENSION_MIN) {
                throw IllegalStateException("tray image height should between $TRAY_IMAGE_DIMENSION_MIN and $TRAY_IMAGE_DIMENSION_MAX pixels, current tray image height is $bitmap.height +, tray image file: $trayImageFile")
            }
            if (bitmap.width > TRAY_IMAGE_DIMENSION_MAX || bitmap.width < TRAY_IMAGE_DIMENSION_MIN) {
                throw IllegalStateException("tray image width should be between $TRAY_IMAGE_DIMENSION_MIN and $TRAY_IMAGE_DIMENSION_MAX pixels, current tray image width is $bitmap.width, tray image file: $trayImageFile")
            }
        } catch (e: Throwable) {
            throw IllegalStateException("Cannot open tray image, $trayImageFile, ${e.localizedMessage}")
        }

        val stickers: List<Sticker> =
            stickerPack.stickers ?: throw IllegalStateException("Stickers in sticker pack identifier: $identifier are null!")
        if (stickers.size < STICKER_SIZE_MIN || stickers.size > STICKER_SIZE_MAX) {
            throw IllegalStateException("sticker pack sticker count should be between 3 to 30 inclusive, it currently has " + stickers.size + ", sticker pack identifier:" + stickerPack.identifier)
        }
        for (sticker in stickers) {
            validateSticker(identifier, sticker)
        }
    }

    @Throws(Throwable::class)
    private fun validateSticker(identifier: String, sticker: Sticker) {
        val emojis = sticker.emojis
            ?: throw IllegalStateException("Emojis is ${sticker.imageFileName} are null instead of empty which shouldn't happen!")
        val imageFileName = sticker.imageFileName

        if (imageFileName.isNullOrBlank()) {
            throw IllegalStateException("no file path for sticker, sticker pack identifier:$identifier")
        }

        if (emojis.size > EMOJI_LIMIT) {
            throw IllegalStateException("emoji count exceed limit, sticker pack identifier:$identifier, filename: $imageFileName")
        }

        validateStickerFile(identifier, imageFileName)
    }

    @Throws(IllegalStateException::class)
    private fun validateStickerFile(identifier: String, fileName: String) {
        try {
            val bytes = fetchStickerAssetUseCase.fetchStickerAsset(identifier, fileName)
            if (bytes.size > STICKER_FILE_SIZE_LIMIT_KB * ONE_KIBIBYTE) {
                throw Exception("sticker should be less than $STICKER_FILE_SIZE_LIMIT_KB KB, sticker pack identifier: $identifier, filename: $fileName")
            }
            try {
                val webPImage = WebPImage.create(bytes)
                if (webPImage.height != IMAGE_HEIGHT) {
                    throw Exception("sticker height should be $IMAGE_HEIGHT, sticker pack identifier:$identifier, filename:$fileName")
                }
                if (webPImage.height != IMAGE_WIDTH) {
                    throw Exception("sticker width should be $IMAGE_WIDTH, sticker pack identifier:$identifier, filename:$fileName")
                }
                if (webPImage.frameCount > 1) {
                    throw Exception(
                        "sticker should be a static image, no animated sticker support at the moment, sticker pack identifier:$identifier, filename:$fileName"
                    )
                }
            } catch (e: Throwable) {
                throw Exception(
                    "Error parsing webp image, sticker pack identifier:$identifier, filename:$fileName ${e.localizedMessage}"
                )
            }
        } catch (e: Throwable) {
            throw Exception(
                "cannot open sticker file: sticker pack identifier:$identifier, filename:$fileName ${e.localizedMessage}"
            )
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
        } catch (e: Throwable) {
            Log.e("StickerPackValidator", "url: $websiteUrl is malformed")
            throw IllegalStateException("url: $websiteUrl is malformed, ${e.localizedMessage}")
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
        } catch (e: Throwable) {
            Log.e("StickerPackValidator", "url: $urlString is malformed")
            throw IllegalStateException("url: $urlString is malformed, ${e.localizedMessage}")
        }

        return false
    }
}