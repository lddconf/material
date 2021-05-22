package com.example.nasaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FragmentSettingsBinding
import com.example.nasaapp.model.IThemeProvider
import com.example.nasaapp.model.ThemeHolder
import com.example.nasaapp.ui.viewmodel.SettingsViewModel


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SettingsFragment : Fragment() {
    private var vb: FragmentSettingsBinding? = null

    private val viewModel: SettingsViewModel by lazy {
        ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }

    private var currentThemeResourceId = ThemeHolder.NasaAppThemes.BlackTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSettingsBinding.inflate(inflater, container, false).also {
        vb = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTheme().observe(viewLifecycleOwner, Observer<ThemeHolder> { theme ->
            setTheme(theme.themeId)
        })
        initAppBar()
        loadThemeSettings()
        initThemeSelector()
    }

    private fun initThemeSelector() {
        vb?.themeSelection?.isChecked = currentThemeResourceId == ThemeHolder.NasaAppThemes.BlackTheme
        vb?.themeSelection?.setOnCheckedChangeListener { buttonView, isChecked ->
            val theme = if (isChecked) {
                ThemeHolder.NasaAppThemes.BlackTheme
            } else {
                ThemeHolder.NasaAppThemes.LightTheme
            }
            viewModel.setTheme(theme)
        }
    }

    private fun setTheme(themeId: ThemeHolder.NasaAppThemes) {
        if (currentThemeResourceId != themeId) {
            (requireActivity() as IThemeProvider).setCurrentTheme(themeId)
        }
    }

    private fun loadThemeSettings() {
        currentThemeResourceId = (activity as IThemeProvider).getCurrentTheme()
        viewModel.setTheme(currentThemeResourceId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vb = null
    }

    private fun initAppBar() {
        setHasOptionsMenu(true)
        vb?.toolbar?.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        vb?.toolbar?.setTitle(R.string.app_name)
        vb?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


}