package com.example.nasaapp.model

sealed class PictureOfTheDayData {
    data class Success(val ofTheDayResponseData: PictureOfTheDayResponseData) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val process: Int?) : PictureOfTheDayData()
}
