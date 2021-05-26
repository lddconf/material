package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.navigation.NavCommands

class MainActivityViewModel : ViewModel() {
    private val currentScreen = MutableLiveData<NavCommands>()
    fun getScreens() : LiveData<NavCommands>  = currentScreen

    fun toPOD() {
        currentScreen.value = NavCommands.PictureOfTheDay
    }

    fun toAbout() {
        currentScreen.value = NavCommands.About
    }

    fun toGallery() {
        currentScreen.value = NavCommands.ImageGallery
    }



}