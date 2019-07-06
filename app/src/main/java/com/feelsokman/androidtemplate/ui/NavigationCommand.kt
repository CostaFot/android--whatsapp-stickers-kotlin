package com.feelsokman.androidtemplate.ui

sealed class NavigationCommand {
    object FirstGlobalCommand : NavigationCommand()
    object SecondGlobalCommand : NavigationCommand()
}