package com.example.nasaapp.ui.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.R
import com.example.nasaapp.model.IThemeProvider
import com.example.nasaapp.model.ThemeHolder

class ThemeProvider(context: Context) : IThemeProvider {
    private val sharedPref by lazy {
        context.getSharedPreferences(sharedPrefSettingsKey, AppCompatActivity.MODE_PRIVATE)
    }
    override var cTheme: ThemeHolder.NasaAppThemes = loadCurrentTheme()
        set(value) {
            saveCurrentTheme(value)
            field = value
        }

    override fun getCurrentThemeResourceID(): Int {
        sharedPref?.let {
            if (it.getInt(themeIdSettings, defaultThemeId.id) > 0) {
                return R.style.Theme_NasaApp_NasaAppStrange_NoActionBar
            } else {
                return R.style.Theme_NasaApp_NoActionBar
            }
        }
        return R.style.Theme_NasaApp_NasaAppStrange_NoActionBar
    }

    private fun loadCurrentTheme(): ThemeHolder.NasaAppThemes {
        sharedPref?.let {
            if (it.getInt(themeIdSettings, defaultThemeId.id) > 0) {
                return ThemeHolder.NasaAppThemes.LightTheme
            } else {
                return ThemeHolder.NasaAppThemes.BlackTheme
            }
        }
        return ThemeHolder.NasaAppThemes.LightTheme
    }

    private fun saveCurrentTheme(themeHolder: ThemeHolder.NasaAppThemes) {
        with(sharedPref.edit()) {
            putInt(themeIdSettings, themeHolder.id)
            apply()
        }
    }

    companion object {
        private const val sharedPrefSettingsKey = "com.example.nasaapp.ui.settings"
        private const val themeIdSettings = "themeId"
        private val defaultThemeId = ThemeHolder.NasaAppThemes.LightTheme
    }

}