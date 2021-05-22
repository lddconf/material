package com.example.nasaapp.model


data class ThemeHolder(
    val themeId: NasaAppThemes
) {
    enum class NasaAppThemes(val id : Int) { BlackTheme(0), LightTheme(1) }
}
