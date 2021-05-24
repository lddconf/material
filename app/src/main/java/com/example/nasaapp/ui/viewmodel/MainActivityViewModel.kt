package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.navigation.NavCommands

class MainActivityViewModel : ViewModel() {
    val currentScreen = MutableLiveData<NavCommands>()

    fun getScreens() : LiveData<NavCommands>  = currentScreen

    fun toPOD() {
        currentScreen.value = NavCommands.PictureOfTheDay
    }

    fun toNews() {
        currentScreen.value = NavCommands.News
    }

    init {
        currentScreen.value = NavCommands.PictureOfTheDay
    }
}