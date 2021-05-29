package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.nasaapp.model.IThemeProvider
import com.example.nasaapp.model.ThemeHolder
import com.example.nasaapp.model.navigation.NavCommands

class SettingsViewModel(private val themeProvider: IThemeProvider?) : ViewModel(), IBackPressedVModel {
    private val liveData: MutableLiveData<ThemeHolder> = MutableLiveData()
    private val navCommands: MutableLiveData<NavCommands> = MutableLiveData()

    fun themeLD(): LiveData<ThemeHolder> = liveData
    fun setTheme(themeId: ThemeHolder.NasaAppThemes) : Boolean {
        themeProvider?.let { provider->
            if ( provider.cTheme != themeId ) {
                provider.cTheme = themeId
                liveData.value = ThemeHolder(themeId)
                return true
            }
        }
        return false
    }

    fun navCommandsLD(): LiveData<NavCommands> = navCommands

    override fun onBackPressed() : Boolean {
        navCommands.value = NavCommands.OnBackCommand
        return true
    }

    private class SettingsViewModelFactory(val themeProvider: IThemeProvider?) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(IThemeProvider::class.java).newInstance(themeProvider)
        }
    }

    companion object {
        fun getSettingsViewModelFactory(themeProvider: IThemeProvider?) : ViewModelProvider.Factory {
            return SettingsViewModelFactory(themeProvider)
        }
    }

    init {
        themeProvider?.let {
            liveData.value = ThemeHolder(themeProvider.cTheme)
        }
    }
}