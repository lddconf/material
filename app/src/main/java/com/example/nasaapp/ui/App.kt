package com.example.nasaapp.ui

import android.app.Application
import com.example.nasaapp.model.IThemeProvider
import com.example.nasaapp.model.dictionary.IWordsDictionary
import com.example.nasaapp.model.dictionary.SimpleNasaDictionary
import com.example.nasaapp.ui.util.ThemeProvider

class App : Application() {
    companion object {
        lateinit var themeProvider : IThemeProvider
            private set

        lateinit var wordsDictionary: IWordsDictionary
            private set
    }

    override fun onCreate() {
        super.onCreate()
        themeProvider = ThemeProvider(this)
        wordsDictionary = SimpleNasaDictionary()
    }
}