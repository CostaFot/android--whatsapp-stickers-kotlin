package com.feelsokman.stickers.contentprovider.model

import android.os.Parcel
import android.os.Parcelable

class Sticker() : Parcelable {

    var imageFileName: String? = null
    var emojis: List<String>? = null
    var sizeSticker: Long = 0

    constructor(imageFileName: String, emojis: List<String>) : this() {
        this.imageFileName = imageFileName
        this.emojis = emojis
    }

    constructor(parcel: Parcel) : this() {
        imageFileName = parcel.readString()
        emojis = parcel.createStringArrayList()
        sizeSticker = parcel.readLong()
    }

    fun setSize(size: Long) {
        this.sizeSticker = size
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(imageFileName)
        dest.writeStringList(emojis)
        dest.writeLong(sizeSticker)
    }

    companion object CREATOR : Parcelable.Creator<Sticker> {
        override fun createFromParcel(parcel: Parcel): Sticker {
            return Sticker(parcel)
        }

        override fun newArray(size: Int): Array<Sticker?> {
            return arrayOfNulls(size)
        }
    }
}
