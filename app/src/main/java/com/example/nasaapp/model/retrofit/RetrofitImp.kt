package com.example.nasaapp.model.retrofit

import com.example.nasaapp.model.PictureOfTheDayData
import com.example.nasaapp.model.api.IPictureOfTheDaySource
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImp : IRetrofitPictureOfTheDay {
    companion object {
        private const val baseUrl = "https://api.nasa.gov/"
    }

    override fun retrofitImpl(): IPictureOfTheDaySource {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            ).build()
        return retrofit.create(IPictureOfTheDaySource::class.java)
    }

    private fun createOkHttpClient(interceptor: Interceptor?): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        interceptor?.let {
            httpClient.addInterceptor(it)
            httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return httpClient.build()
    }

    inner class PictureOfTheDayInterceptor() : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.proceed(chain.request())
        }
    }
}