package com.example.nasaapp.ui

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.nasaapp.R
import com.example.nasaapp.model.IThemeProvider
import com.example.nasaapp.model.ThemeHolder

class MainActivity : AppCompatActivity(), IThemeProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getThemeResourceId())
        setContentView(R.layout.activity_main)
    }

    override fun getThemeResourceId(): Int {
        val sharedPref = getSharedPreferences(sharedPrefSettingsKey, MODE_PRIVATE)
        sharedPref?.let {
            if ( it.getInt(themeIdSettings, defaultThemeId.id) > 0 ) {
                //ThemeHolder.NasaAppThemes.LightTheme
                return R.style.Theme_NasaApp_NasaAppStrange_NoActionBar
            } else {
                return R.style.Theme_NasaApp_NoActionBar
            }
        }
        return R.style.Theme_NasaApp_NasaAppStrange_NoActionBar
    }

    override fun getCurrentTheme(): ThemeHolder.NasaAppThemes {
        val sharedPref = getSharedPreferences(sharedPrefSettingsKey, MODE_PRIVATE)
        sharedPref?.let {
            if ( it.getInt(themeIdSettings, defaultThemeId.id) > 0 ) {
                return ThemeHolder.NasaAppThemes.LightTheme
            } else {
                return ThemeHolder.NasaAppThemes.BlackTheme
            }
        }
        return ThemeHolder.NasaAppThemes.LightTheme
    }

    override fun setCurrentTheme(themeHolder: ThemeHolder.NasaAppThemes) {
        val sharedPref = getSharedPreferences(sharedPrefSettingsKey, MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(themeIdSettings, themeHolder.id )
            apply()
        }
        this.recreate()
    }

    companion object {
        private const val sharedPrefSettingsKey = "com.example.nasaapp.ui.settings"
        private const val themeIdSettings = "themeId"
        private val defaultThemeId = ThemeHolder.NasaAppThemes.LightTheme
    }
}