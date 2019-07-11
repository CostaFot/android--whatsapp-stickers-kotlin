package com.feelsokman.stickers.usecase

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import androidx.annotation.NonNull
import com.feelsokman.domain.error.DataSourceErrorKind
import com.feelsokman.net.domain.error.DataSourceError
import com.feelsokman.net.domain.usecases.BaseDisposableUseCase
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.ANDROID_APP_DOWNLOAD_LINK_IN_QUERY
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.IOS_APP_DOWNLOAD_LINK_IN_QUERY
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.LICENSE_AGREEMENT_WEBSITE
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.PRIVACY_POLICY_WEBSITE
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.PUBLISHER_EMAIL
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.PUBLISHER_WEBSITE
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.STICKER_FILE_EMOJI_IN_QUERY
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.STICKER_FILE_NAME_IN_QUERY
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.STICKER_PACK_ICON_IN_QUERY
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.STICKER_PACK_IDENTIFIER_IN_QUERY
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.STICKER_PACK_NAME_IN_QUERY
import com.feelsokman.stickers.contentprovider.StickerContentProvider.Companion.STICKER_PACK_PUBLISHER_IN_QUERY
import com.feelsokman.stickers.contentprovider.model.Sticker
import com.feelsokman.stickers.contentprovider.model.StickerPack
import com.feelsokman.stickers.contentprovider.utils.StickerPackValidator
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.HashSet

class StickerPackLoaderUseCase(
    private val scheduler: Scheduler,
    private val contentResolver: ContentResolver,
    private val providerAuthority: String,
    private val uriResolverUseCase: UriResolverUseCase,
    private val stickerPackValidator: StickerPackValidator
) : BaseDisposableUseCase() {

    override val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override var latestDisposable: Disposable? = null

    fun loadStickerPacks(callback: Callback<ArrayList<StickerPack>>) {
        latestDisposable?.dispose()
        latestDisposable =
            Single.fromCallable { fetchStickerPacks() }
                .subscribeOn(scheduler)
                .doOnSubscribe { compositeDisposable.add(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { stickerPacks -> callback.onSuccess(stickerPacks) },
                    { error -> callback.onError(DataSourceError(error.localizedMessage, DataSourceErrorKind.UNEXPECTED)) }
                )
    }

    @SuppressLint("Recycle")
    @Throws(IllegalStateException::class)
    private fun fetchStickerPacks(): ArrayList<StickerPack> {
        val cursor = contentResolver.query(uriResolverUseCase.getAuthorityUri(), null, null, null, null)
            ?: throw IllegalStateException("could not fetch from content provider $providerAuthority")
        val identifierSet = HashSet<String>()
        val stickerPackList = fetchFromContentProvider(cursor)
        for (stickerPack in stickerPackList) {
            if (identifierSet.contains(stickerPack.identifier)) {
                throw IllegalStateException("sticker pack identifiers should be unique, there are more than one pack with identifier:" + stickerPack.identifier)
            } else {
                identifierSet.add(stickerPack.identifier!!)
            }
        }
        if (stickerPackList.isEmpty()) {
            throw IllegalStateException("There should be at least one sticker pack in the app")
        }
        for (stickerPack in stickerPackList) {
            val stickers = getStickersForPack(stickerPack)
            stickerPack.resetStickers(stickers)
            stickerPackValidator.verifyStickerPackValidity(stickerPack)
        }
        return stickerPackList
    }

    @NonNull
    private fun getStickersForPack(stickerPack: StickerPack): List<Sticker> {
        val stickers = fetchFromContentProviderForStickers(stickerPack.identifier!!, contentResolver)
        for (sticker in stickers) {
            val bytes: ByteArray
            try {
                bytes = fetchStickerAsset(stickerPack.identifier!!, sticker.imageFileName!!, contentResolver)
                if (bytes.isEmpty()) {
                    throw IllegalStateException("Asset file is empty, pack: " + stickerPack.name + ", sticker: " + sticker.imageFileName)
                }
                sticker.size = bytes.size.toLong()
            } catch (e: Throwable) {
                throw IllegalStateException(
                    "Asset file doesn't exist. pack: " + stickerPack.name + ", sticker: " + sticker.imageFileName,
                    e
                )
            } catch (e: Throwable) {
                throw IllegalStateException(
                    "Asset file doesn't exist. pack: " + stickerPack.name + ", sticker: " + sticker.imageFileName,
                    e
                )
            }
        }
        return stickers
    }

    @NonNull
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
                identifier,
                name,
                publisher,
                trayImage,
                publisherEmail,
                publisherWebsite,
                privacyPolicyWebsite,
                licenseAgreementWebsite
            )
            stickerPack.androidPlayStoreLink = androidPlayStoreLink
            stickerPack.iosAppStoreLink = iosAppLink
            stickerPackList.add(stickerPack)
        } while (cursor.moveToNext())
        return stickerPackList
    }

    @NonNull
    private fun fetchFromContentProviderForStickers(identifier: String, contentResolver: ContentResolver): List<Sticker> {
        val uri = uriResolverUseCase.getStickerListUri(identifier)

        val projection = arrayOf<String>(STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val stickers = ArrayList<Sticker>()
        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_FILE_NAME_IN_QUERY))
                val emojisConcatenated = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_FILE_EMOJI_IN_QUERY))
                stickers.add(
                    Sticker(
                        name,
                        listOf(*emojisConcatenated.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return stickers
    }

    @Throws(IOException::class)
    fun fetchStickerAsset(@NonNull identifier: String, @NonNull name: String, contentResolver: ContentResolver): ByteArray {
        contentResolver.openInputStream(uriResolverUseCase.getStickerAssetUri(identifier, name))!!.use { inputStream ->
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

    override fun stopAllBackgroundWork() {
        compositeDisposable.clear()
    }
}