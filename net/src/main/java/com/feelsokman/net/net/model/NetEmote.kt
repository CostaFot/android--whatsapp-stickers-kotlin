package com.feelsokman.net.net.model

import com.google.gson.annotations.SerializedName

data class NetEmote(
    @SerializedName("channel")
    val channel: Any?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("restrictions")
    val restrictions: Restrictions
)