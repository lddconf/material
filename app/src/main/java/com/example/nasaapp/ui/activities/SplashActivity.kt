package com.example.nasaapp.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.nasaapp.databinding.ActivitySplashBinding
import com.example.nasaapp.ui.App
import com.example.nasaapp.ui.activities.MainActivity
import com.example.nasaapp.ui.viewmodel.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(App.themeProvider.getCurrentThemeResourceID())
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.splashOnNext?.setOnClickListener {
            viewModel.startBtnClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.viewStateSubscription().observe(this, Observer {
            renderData(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun renderData(state: SplashViewState) {
        when (state) {
            is SplashInitView -> {
                showNasaLogo()
            }
            is SplashEndView -> {
                runMainActivity()
            }
        }
    }

    private fun showNasaLogo() {
        binding?.apply {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val offset = displayMetrics.heightPixels.toFloat()


            nasaLogo.translationY -= offset
            splashOnNext.translationY += offset

            nasaLogo.visibility = View.VISIBLE
            splashOnNext.visibility = View.VISIBLE

            ObjectAnimator.ofFloat(
                nasaLogo,
                "translationY",
                nasaLogo.translationY,
                nasaLogo.translationY + offset
            ).apply {
                interpolator = BounceInterpolator()
                duration = 3000
            }.start()

            splashOnNext.animate()
                .translationYBy(-offset)
                .setDuration(3000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        splashOnNext.isEnabled = true
                    }
                })
        }
    }

    private fun runMainActivity() {
        val intent = MainActivity.getIntent(applicationContext)
        startActivity(intent)
        finish()
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SplashActivity::class.java)
    }


}