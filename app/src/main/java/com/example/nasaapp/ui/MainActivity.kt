package com.example.nasaapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ActivityMainBinding
import com.example.nasaapp.model.navigation.NavCommands
import com.example.nasaapp.ui.fragments.IBackPressableFragment
import com.example.nasaapp.ui.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private var vb : ActivityMainBinding? = null
    private val navHostFragment by lazy {
        supportFragmentManager.fragments.first() as? NavHostFragment
    }
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setTheme(App.themeProvider.getCurrentThemeResourceID())
        setContentView(vb?.root)
        viewModelNavigationSetup()
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        vb?.bottomNavigation?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_pod -> {
                    viewModel.toPOD()
                    true
                }
                R.id.page_gallery -> {
                    viewModel.toGallery()
                    true
                }
                R.id.about_fragment -> {
                    viewModel.toAbout()
                    true
                }
                else -> false
            }
        }
    }

    private fun viewModelNavigationSetup() {
        viewModel.getScreens().observe(this, Observer { navCommands ->
            when (navCommands) {
                is NavCommands.PictureOfTheDay -> {
                    navHostFragment?.findNavController()?.navigate(R.id.pod_view_pager_fragment)
                    if ( vb?.bottomNavigation?.selectedItemId != R.id.page_pod ) {
                        vb?.bottomNavigation?.selectedItemId = R.id.page_pod
                    }

                }
                is NavCommands.ImageGallery -> {
                    navHostFragment?.findNavController()?.navigate(R.id.gallery_fragment)
                    if ( vb?.bottomNavigation?.selectedItemId != R.id.gallery_fragment ) {
                        vb?.bottomNavigation?.selectedItemId = R.id.gallery_fragment
                    }
                }
                is NavCommands.About -> {
                    navHostFragment?.findNavController()?.navigate(R.id.about_fragment)
                    if ( vb?.bottomNavigation?.selectedItemId != R.id.gallery_fragment ) {
                        vb?.bottomNavigation?.selectedItemId = R.id.gallery_fragment
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        navHostFragment?.childFragmentManager?.fragments?.forEach { fragment ->
            if (fragment is IBackPressableFragment) {
                if (fragment.backPressed()) {
                    return
                }
            }
        }
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        vb = null
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}