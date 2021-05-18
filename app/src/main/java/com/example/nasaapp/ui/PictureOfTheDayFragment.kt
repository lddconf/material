package com.example.nasaapp.ui

import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.nasaapp.R
import com.example.nasaapp.model.PictureOfTheDayData
import com.example.nasaapp.ui.viewmodel.PictureOfTheDayViewModel


class PictureOfTheDayFragment : Fragment() {

    private val viewModel : PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        viewModel.getData().observe(viewLifecycleOwner, { pod ->
            renderData(pod)
        })
    }

    private fun renderData(pod : PictureOfTheDayData) {
        when (pod) {
            is PictureOfTheDayData.Success-> {
                val url = pod.ofTheDayResponseData.url
                if ( url.isNullOrEmpty() ) {


                } else {

                }
            }
            is PictureOfTheDayData.Error->{
                showError(pod.error.message)
            }
            is PictureOfTheDayData.Loading->{
                //Load Data
            }
        }
    }


    private fun showError(message: String?) {
        message?.let { message
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

}