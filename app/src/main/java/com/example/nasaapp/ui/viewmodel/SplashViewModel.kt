package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
    private val liveData = MutableLiveData<SplashViewState>()

    fun viewStateSubscription() : LiveData<SplashViewState> = liveData

    override fun onCleared() {
        super.onCleared()
    }

    fun startBtnClicked() {
        liveData.value = SplashEndView
    }

    init {
        liveData.value = SplashInitView
    }
}


sealed class SplashViewState

object SplashInitView : SplashViewState()
object SplashEndView : SplashViewState()
