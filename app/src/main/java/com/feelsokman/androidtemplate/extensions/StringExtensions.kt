package com.feelsokman.androidtemplate.extensions

fun String.onlyDigits(): String = replace(Regex("\\D*"), "")

fun String.removeAllSpecialCharacters(): String = replace("[^a-zA-Z]+".toRegex(), "")