package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.model.PictureOfTheDayData
import com.example.nasaapp.model.PictureOfTheDayResponseData
import com.example.nasaapp.model.retrofit.IRetrofitPictureOfTheDay
import com.example.nasaapp.model.retrofit.RetrofitImp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PictureOfTheDayViewModel() : ViewModel(), IBackPressedVModel {
    private val liveData: MutableLiveData<PictureOfTheDayData> = MutableLiveData()
    private val isSearchMode : MutableLiveData<Boolean> = MutableLiveData()

    private val retrofitImp: IRetrofitPictureOfTheDay = RetrofitImp()

    fun getData(): LiveData<PictureOfTheDayData> {
        sendServerRequest()
        return liveData
    }

    fun getWikiSearchMode() : LiveData<Boolean> = isSearchMode

    fun inWikiSearchRequested()  {
        isSearchMode.value = true
    }

    fun disableWikiSearchMode() {
        isSearchMode.value = false
    }

    fun appSettingsRequested() {
        liveData.value = PictureOfTheDayData.Settings
    }

    fun performWikiSearch(word : String) {
        disableWikiSearchMode()
        liveData.value = PictureOfTheDayData.PerformWikiSearch(word)
    }

    private fun sendServerRequest() {
        liveData.value = PictureOfTheDayData.Loading(null)
        val apiKey = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("Need Nasa API key"))
        } else {
            retrofitImp.retrofitImpl().pictureOfTheDay(apiKey).enqueue(
                object : Callback<PictureOfTheDayResponseData> {
                    override fun onResponse(
                        call: Call<PictureOfTheDayResponseData>,
                        response: Response<PictureOfTheDayResponseData>
                    ) {
                        liveData.value = if (response.isSuccessful && response.body() != null) {
                            PictureOfTheDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                PictureOfTheDayData.Error(Throwable("Api internal error"))
                            } else {
                                PictureOfTheDayData.Error(Throwable(Throwable(message)))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {
                        liveData.value = PictureOfTheDayData.Error(t)
                    }
                })
        }
    }

    override fun onBackPressed() : Boolean {
        isSearchMode.value?.let { status ->
            if ( status ) {
                isSearchMode.value = false
                return true
            }
        }
        return false
    }

    init {
        isSearchMode.value = false
    }

}