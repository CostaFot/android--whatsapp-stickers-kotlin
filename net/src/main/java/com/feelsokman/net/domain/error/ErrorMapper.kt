package com.feelsokman.net.domain.error

interface ErrorMapper<in T> {
    fun convert(error: T?): DataSourceError
}
