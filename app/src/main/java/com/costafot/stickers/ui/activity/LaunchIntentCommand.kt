package com.costafot.stickers.ui.activity

import android.content.Intent

sealed class LaunchIntentCommand {
    data class Chooser(val intent: Intent) : LaunchIntentCommand()
    data class Specific(val intent: Intent) : LaunchIntentCommand()
}