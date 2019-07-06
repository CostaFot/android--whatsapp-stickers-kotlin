package com.feelsokman.net.net.error

import com.google.gson.annotations.SerializedName

class RestErrorBody(
    @SerializedName("message") val message: String?,
    @SerializedName("errors") val errors: List<RestError?>?,
    @SerializedName("status_code") val statusCode: Int?
)
