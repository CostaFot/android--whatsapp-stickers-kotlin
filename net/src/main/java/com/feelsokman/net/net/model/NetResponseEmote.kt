package com.feelsokman.net.net.model

import com.google.gson.annotations.SerializedName

data class NetResponseEmote(
    @SerializedName("emotes")
    val netEmotes: List<NetEmote>?,
    @SerializedName("status")
    val status: Int,
    @SerializedName("urlTemplate")
    val urlTemplate: String
)