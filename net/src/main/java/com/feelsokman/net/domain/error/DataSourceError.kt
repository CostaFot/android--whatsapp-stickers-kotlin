package com.feelsokman.net.domain.error

import com.feelsokman.domain.error.DataSourceErrorKind
import java.util.HashMap

data class DataSourceError(val errorMessage: String?, val kind: DataSourceErrorKind) {
    val warnings: MutableMap<String, String> = HashMap()

    fun addWarning(key: String, value: String) {
        warnings[key] = value
    }
}
