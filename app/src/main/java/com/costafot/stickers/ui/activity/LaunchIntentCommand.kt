package com.costafot.stickers.ui.activity

sealed class LaunchIntentCommand {
    data class Chooser(val identifier: String, val packName: String) : LaunchIntentCommand()
    data class Specific(val identifier: String, val packName: String, val specificPackage: String) : LaunchIntentCommand()
}