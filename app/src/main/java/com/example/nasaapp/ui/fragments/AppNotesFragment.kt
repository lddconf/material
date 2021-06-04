package com.example.nasaapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nasaapp.R
import com.example.nasaapp.ui.viewmodel.ImageGalleryViewModel

class AppNotesFragment : Fragment() {
    companion object {
        fun newInstance() = AppNotesFragment()
    }

    private lateinit var viewModel: ImageGalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.app_notes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImageGalleryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}