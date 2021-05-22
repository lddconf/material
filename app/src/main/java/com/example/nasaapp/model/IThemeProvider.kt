package com.example.nasaapp.model

interface IThemeProvider {
    fun getThemeResourceId() : Int
    fun getCurrentTheme() : ThemeHolder.NasaAppThemes

    fun setCurrentTheme(themeHolder: ThemeHolder.NasaAppThemes)
}