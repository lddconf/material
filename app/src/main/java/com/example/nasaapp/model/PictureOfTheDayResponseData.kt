package com.example.nasaapp.model

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType

data class PictureOfTheDayResponseData(
    @field:SerializedName("date") val date : String?,
    @field:SerializedName("explanation") val explanation : String?,
    @field:SerializedName("hdurl") val hdurl : String?,
    @field:SerializedName("mediaType") val mediaType: String?,
    @field:SerializedName("serviceVersion") val serviceVersion : String?,
    @field:SerializedName("title") val title : String?,
    @field:SerializedName("url") val url : String?,
    @field:SerializedName("copyright") val copyright : String?
)
