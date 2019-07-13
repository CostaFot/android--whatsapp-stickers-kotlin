package com.costafot.stickers.usecase

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import com.costafot.stickers.contentprovider.StickerProviderHelper

class WhiteListCheckUseCase(
    private val stickerProviderHelper: StickerProviderHelper,
    private val packageManager: PackageManager
) {

    companion object {
        private const val AUTHORITY_QUERY_PARAM = "authority"
        private const val IDENTIFIER_QUERY_PARAM = "identifier"
        private const val CONSUMER_WHATSAPP_PACKAGE_NAME = "com.whatsapp"
        private const val SMB_WHATSAPP_PACKAGE_NAME = "com.whatsapp.w4b"
        private const val CONTENT_PROVIDER = ".provider.sticker_whitelist_check"
        private const val QUERY_PATH = "is_whitelisted"
        private const val QUERY_RESULT_COLUMN_NAME = "result"
    }

    fun isWhitelisted(identifier: String): Boolean {
        try {
            if (!isWhatsAppConsumerAppInstalled(packageManager) && !isWhatsAppSmbAppInstalled(packageManager)) {
                return false
            }
            val consumerResult = isStickerPackWhitelistedInWhatsAppConsumer(identifier)
            val smbResult = isStickerPackWhitelistedInWhatsAppSmb(identifier)
            return consumerResult && smbResult
        } catch (e: Exception) {
            return false
        }
    }

    private fun isWhitelistedFromProvider(identifier: String, whatsappPackageName: String): Boolean {
        if (isPackageInstalled(whatsappPackageName, packageManager)) {
            val whatsappProviderAuthority = whatsappPackageName + CONTENT_PROVIDER
            packageManager.resolveContentProvider(whatsappProviderAuthority, PackageManager.GET_META_DATA) ?: return false
            // provider is not there. The WhatsApp app may be an old version.
            val queryUri =
                Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(whatsappProviderAuthority).appendPath(QUERY_PATH)
                    .appendQueryParameter(AUTHORITY_QUERY_PARAM, stickerProviderHelper.providerAuthority)
                    .appendQueryParameter(IDENTIFIER_QUERY_PARAM, identifier).build()
            stickerProviderHelper.contentResolver.query(queryUri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val whiteListResult = cursor.getInt(cursor.getColumnIndexOrThrow(QUERY_RESULT_COLUMN_NAME))
                    cursor.close()
                    return whiteListResult == 1
                }
            }
        } else {
            // if app is not installed, then don't need to take into its whitelist info into account.
            return true
        }
        return false
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            applicationInfo?.enabled ?: false
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun isWhatsAppConsumerAppInstalled(packageManager: PackageManager): Boolean {
        return isPackageInstalled(CONSUMER_WHATSAPP_PACKAGE_NAME, packageManager)
    }

    private fun isWhatsAppSmbAppInstalled(packageManager: PackageManager): Boolean {
        return isPackageInstalled(SMB_WHATSAPP_PACKAGE_NAME, packageManager)
    }

    private fun isStickerPackWhitelistedInWhatsAppConsumer(identifier: String): Boolean {
        return isWhitelistedFromProvider(identifier, CONSUMER_WHATSAPP_PACKAGE_NAME)
    }

    private fun isStickerPackWhitelistedInWhatsAppSmb(identifier: String): Boolean {
        return isWhitelistedFromProvider(identifier, SMB_WHATSAPP_PACKAGE_NAME)
    }
}