package com.costafot.stickers.ui

sealed class NavigationCommand {
    object FirstGlobalCommand : NavigationCommand()
    object SecondGlobalCommand : NavigationCommand()
}