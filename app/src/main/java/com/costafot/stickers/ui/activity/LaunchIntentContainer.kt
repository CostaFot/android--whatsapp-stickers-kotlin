package com.costafot.stickers.ui.activity

sealed class LaunchIntentContainer {
    data class Chooser(val identifier: String, val packName: String) : LaunchIntentContainer()
    data class Specific(val identifier: String, val packName: String, val specificPackage: String) : LaunchIntentContainer()
}