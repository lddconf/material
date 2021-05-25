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


class PODViewPagerViewModel() : ViewModel(), IBackPressedVModel {
    private val liveData: MutableLiveData<PictureOfTheDayData> = MutableLiveData()
    private val isSearchMode: MutableLiveData<Boolean> = MutableLiveData()

    private val retrofitImp: IRetrofitPictureOfTheDay = RetrofitImp()

    fun getData(): LiveData<PictureOfTheDayData> {
        return liveData
    }

    fun getWikiSearchMode(): LiveData<Boolean> = isSearchMode

    fun inWikiSearchRequested() {
        isSearchMode.value = true
    }

    fun disableWikiSearchMode() {
        isSearchMode.value = false
    }

    fun appSettingsRequested() {
        liveData.value = PictureOfTheDayData.Settings
    }

    fun performWikiSearch(word: String) {
        disableWikiSearchMode()
        liveData.value = PictureOfTheDayData.PerformWikiSearch(word)
    }

    override fun onBackPressed(): Boolean {
        isSearchMode.value?.let { status ->
            if (status) {
                isSearchMode.value = false
                return true
            }
        }
        return false
    }

    init {
        isSearchMode.value = false
    }



//    inner class BottomNavigationBehavior : CoordinatorLayout.Behavior<BottomNavigationView>() {
//        override fun layoutDependsOn(
//            parent: CoordinatorLayout,
//            child: BottomNavigationView,
//            dependency: View
//        ): Boolean {
//            return dependency is BottomNavigationView
//        }
//
//        override fun onDependentViewChanged(
//            parent: CoordinatorLayout,
//            child: BottomNavigationView,
//            dependency: View
//        ): Boolean {
//            return super.onDependentViewChanged(parent, child, dependency)
//        }
//
//        private fun modifyBottomNavigationView(
//            bottomNavigationView: BottomNavigationView,
//            dependency: View
//        ) {
//            bottomNavigationView.y = bottomNavigationView.y
//        }
//    }

}