package com.feelsokman.net.net.error

import com.google.gson.annotations.SerializedName

class RestError(
    @SerializedName("key") val key: String?,
    @SerializedName("message") val message: String?
)
