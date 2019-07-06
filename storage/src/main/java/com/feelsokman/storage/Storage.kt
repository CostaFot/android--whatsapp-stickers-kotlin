package com.feelsokman.storage

interface Storage {

    fun saveSampleString(text: String)

    fun getSampleString(): String
}