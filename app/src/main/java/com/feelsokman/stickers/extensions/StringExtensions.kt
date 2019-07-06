package com.feelsokman.stickers.extensions

fun String.onlyDigits(): String = replace(Regex("\\D*"), "")

fun String.removeAllSpecialCharacters(): String = replace("[^a-zA-Z]+".toRegex(), "")