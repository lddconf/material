package com.example.nasaapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.nasaapp.model.IThemeProvider
import com.example.nasaapp.model.ThemeHolder
import com.example.nasaapp.model.navigation.NavCommands

class SettingsViewModel(private val themeProvider: IThemeProvider?) : ViewModel() {
    private val liveData: MutableLiveData<ThemeHolder> = MutableLiveData()
    private val navCommands: MutableLiveData<NavCommands> = MutableLiveData()

    fun themeLD(): LiveData<ThemeHolder> = liveData
    fun setTheme(themeId: ThemeHolder.NasaAppThemes) {
        themeProvider?.let { provider->
            if ( provider.getCurrentTheme() != themeId ) {
                liveData.value = ThemeHolder(themeId)
                provider.setCurrentTheme(themeId)
            }
        }
    }

    fun navCommandsLD(): LiveData<NavCommands> = navCommands
    fun onBackPressed() {
        navCommands.value = NavCommands.OnBackCommand
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
            liveData.value = ThemeHolder(themeProvider.getCurrentTheme())
        }
    }
}