package com.example.nasaapp.model.api

import com.example.nasaapp.model.PictureOfTheDayResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IPictureOfTheDaySource {
    @GET("planetary/apod")
    fun pictureOfTheDay(@Query("api_key") apiKey : String) : Call<PictureOfTheDayResponseData>
}