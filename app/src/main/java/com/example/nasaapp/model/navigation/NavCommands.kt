package com.example.nasaapp.model.navigation

sealed class NavCommands {
    //On back pressed command
    object OnBackCommand : NavCommands()
    object PictureOfTheDay : NavCommands()
    object About : NavCommands()
    object ImageGallery : NavCommands()
}

