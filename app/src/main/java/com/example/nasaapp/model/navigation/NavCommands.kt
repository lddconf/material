package com.example.nasaapp.model.navigation

sealed class NavCommands {
    //On back pressed command
    object OnBackCommand : NavCommands()
    object PictureOfTheDay : NavCommands()
    object News : NavCommands()
}

