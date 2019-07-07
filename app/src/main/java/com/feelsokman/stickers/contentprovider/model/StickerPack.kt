package com.feelsokman.stickers.contentprovider.model

import android.os.Parcel
import android.os.Parcelable

class StickerPack : Parcelable {

    var identifier: String? = null
    var name: String? = null
    var publisher: String? = null
    var trayImageFile: String? = null
    val publisherEmail: String?
    val publisherWebsite: String?
    val privacyPolicyWebsite: String?
    val licenseAgreementWebsite: String?

    var iosAppStoreLink: String? = null
    var stickers: List<Sticker>? = null
    var totalSize: Long = 0
        private set
    var androidPlayStoreLink: String? = null
    var isWhitelisted: Boolean = false

    constructor(
        identifier: String,
        name: String,
        publisher: String,
        trayImageFile: String,
        publisherEmail: String,
        publisherWebsite: String,
        privacyPolicyWebsite: String,
        licenseAgreementWebsite: String
    ) {
        this.identifier = identifier
        this.name = name
        this.publisher = publisher
        this.trayImageFile = trayImageFile
        this.publisherEmail = publisherEmail
        this.publisherWebsite = publisherWebsite
        this.privacyPolicyWebsite = privacyPolicyWebsite
        this.licenseAgreementWebsite = licenseAgreementWebsite
    }

    constructor(parcel: Parcel) {
        identifier = parcel.readString()
        name = parcel.readString()
        publisher = parcel.readString()
        trayImageFile = parcel.readString()
        publisherEmail = parcel.readString()
        publisherWebsite = parcel.readString()
        privacyPolicyWebsite = parcel.readString()
        licenseAgreementWebsite = parcel.readString()
        iosAppStoreLink = parcel.readString()
        stickers = parcel.createTypedArrayList(Sticker.CREATOR)
        totalSize = parcel.readLong()
        androidPlayStoreLink = parcel.readString()
        isWhitelisted = parcel.readByte().toInt() != 0
    }

    // This is setStickers in the other app
    fun resetStickers(stickers: List<Sticker>) {
        this.stickers = stickers
        totalSize = 0
        for (sticker in stickers) {
            totalSize += sticker.size
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(identifier)
        dest.writeString(name)
        dest.writeString(publisher)
        dest.writeString(trayImageFile)
        dest.writeString(publisherEmail)
        dest.writeString(publisherWebsite)
        dest.writeString(privacyPolicyWebsite)
        dest.writeString(licenseAgreementWebsite)
        dest.writeString(iosAppStoreLink)
        dest.writeTypedList(stickers)
        dest.writeLong(totalSize)
        dest.writeString(androidPlayStoreLink)
        dest.writeByte((if (isWhitelisted) 1 else 0).toByte())
    }

    companion object CREATOR : Parcelable.Creator<StickerPack> {
        override fun createFromParcel(parcel: Parcel): StickerPack {
            return StickerPack(parcel)
        }

        override fun newArray(size: Int): Array<StickerPack?> {
            return arrayOfNulls(size)
        }
    }
}