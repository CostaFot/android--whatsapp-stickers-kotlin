package com.feelsokman.net.net.model

import com.google.gson.annotations.SerializedName

data class Restrictions(
    @SerializedName("channels")
    val channels: List<Any>,
    @SerializedName("games")
    val games: List<Any>
)