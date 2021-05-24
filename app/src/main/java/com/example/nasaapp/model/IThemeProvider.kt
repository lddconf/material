package com.example.nasaapp.model

interface IThemeProvider {
    var cTheme: ThemeHolder.NasaAppThemes
    fun getCurrentThemeResourceID() : Int
}