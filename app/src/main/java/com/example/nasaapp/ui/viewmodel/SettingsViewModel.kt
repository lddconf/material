package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.ThemeHolder


class SettingsViewModel() : ViewModel() {
    private val liveData: MutableLiveData<ThemeHolder> = MutableLiveData()

    fun getTheme(): LiveData<ThemeHolder> = liveData

    fun setTheme(themeId: ThemeHolder.NasaAppThemes) {
        liveData.value = ThemeHolder(themeId)
    }

//    init {
//        liveData.value = ThemeHolder(ThemeHolder.NasaAppThemes.BlackTheme)
//    }

}