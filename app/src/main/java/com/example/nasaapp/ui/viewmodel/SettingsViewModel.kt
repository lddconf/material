package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.ThemeHolder
import com.example.nasaapp.model.navigation.NavCommands

class SettingsViewModel() : ViewModel() {
    private val liveData: MutableLiveData<ThemeHolder> = MutableLiveData()
    private val navCommands: MutableLiveData<NavCommands> = MutableLiveData()

    fun themeLD(): LiveData<ThemeHolder> = liveData
    fun setTheme(themeId: ThemeHolder.NasaAppThemes) {
        liveData.value = ThemeHolder(themeId)
    }

    fun navCommandsLD(): LiveData<NavCommands> = navCommands
    fun onBackPressed() {
        navCommands.value = NavCommands.OnBackCommand
    }
}