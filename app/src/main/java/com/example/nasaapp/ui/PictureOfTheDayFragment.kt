package com.example.nasaapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.nasaapp.R
import com.example.nasaapp.databinding.PictureOfTheDayFragmentBinding
import com.example.nasaapp.model.PictureOfTheDayData
import com.example.nasaapp.ui.viewmodel.PictureOfTheDayViewModel
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PictureOfTheDayFragment : Fragment() {
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }
    private var vb: PictureOfTheDayFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = PictureOfTheDayFragmentBinding.inflate(inflater, container, false).also {
        vb = it
        initAppBar()
    }.root

    private fun initAppBar() {
        setHasOptionsMenu(true)
        vb?.toolbar?.inflateMenu(R.menu.picture_of_the_day_menu)
        vb?.toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    showMessage("Searchind...")
                    true
                }
                R.id.action_settings -> {
                    showMessage("Settings")
                    true
                }
                else -> false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { pod ->
            renderData(pod)
        })
    }



    private fun renderData(pod: PictureOfTheDayData) {
        when (pod) {
            is PictureOfTheDayData.Success -> {
                val url = pod.ofTheDayResponseData.url
                if (url.isNullOrEmpty()) {
                    //Load image data
                    showMessage("Picture of the day url not found")
                } else {
                    //Load data into container
                    loadImage(url)
                    vb?.bottomSheetPodDetails?.bottomSheetPodDetailsLayout?.apply {
                        val behavior = BottomSheetBehavior.from(this)
                        val height = vb?.root?.height?:0 - ( vb?.appBar?.height ?: 0) - (vb?.imageView?.height?:0)
                        behavior.peekHeight = height
                    }

                    vb?.bottomSheetPodDetails?.bottomSheetPodDescriptionHeader?.text = pod.ofTheDayResponseData.title
                    vb?.bottomSheetPodDetails?.bottomSheetPodDescription?.text = pod.ofTheDayResponseData.explanation


                }
            }
            is PictureOfTheDayData.Error -> {
                showMessage(pod.error.message)
            }
            is PictureOfTheDayData.Loading -> {
                //Load Data
            }
        }
    }

    private fun loadImage(url: String) {
        vb?.imageView?.load(url) {
            lifecycle(this@PictureOfTheDayFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
        }
    }

    private fun startWikiSearch(text: String) {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://en.wikipedia.org/wiki/$text")
        })
    }

    private fun showMessage(message: String?) {
        message?.let {
            message
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vb = null
    }
}