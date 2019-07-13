package com.costafot.stickers.usecase.error

import java.util.HashMap

data class DataSourceError(val errorMessage: String?, val kind: DataSourceErrorKind) {
    val warnings: MutableMap<String, String> = HashMap()

    fun addWarning(key: String, value: String) {
        warnings[key] = value
    }
}
