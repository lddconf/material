package com.example.nasaapp.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.PictureOfTheDayFragmentBinding
import com.example.nasaapp.model.PictureOfTheDayData
import com.example.nasaapp.ui.App
import com.example.nasaapp.ui.util.getColorFromAttr
import com.example.nasaapp.ui.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment(val lastDayOffset: Int = 0) : Fragment(), IBackPressableFragment {
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this, getPODViewModelFactory(lastDayOffset))
            .get(PictureOfTheDayViewModel::class.java)
    }
    private var vb: PictureOfTheDayFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = PictureOfTheDayFragmentBinding.inflate(inflater, container, false).also {
        vb = it
    }.root

    private fun initBottomSheet() {
        vb?.bottomSheetPodDetails?.bottomSheetPodDetailsLayout?.apply {
            val behavior = BottomSheetBehavior.from(this)
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            vb?.bottomSheetPodDetails?.bottomSheetPodState?.isChecked = false
                        }
                        else -> vb?.bottomSheetPodDetails?.bottomSheetPodState?.isChecked = true
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })

            //Install fronts
            vb?.bottomSheetPodDetails?.bottomSheetPodDescriptionHeader?.typeface =
                Typeface.createFromAsset(
                    requireActivity().assets,
                    "font/dancing_script_variable_font_wght.ttf"
                )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { pod ->
            renderData(pod)
        })
        initBottomSheet()
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
                    vb?.apply {
                        bottomSheetPodDetails.apply {
                            bottomSheetPodDetailsLayout.apply {
                                val behavior = BottomSheetBehavior.from(this)
                                var peekRevertHeightPx = 0
                                vb?.imageView?.let { view ->
                                    peekRevertHeightPx += view.y.toInt() + view.height
                                }
                                val layoutHeight = vb?.podDetailsLayout?.height ?: 0
                                behavior.peekHeight = layoutHeight - peekRevertHeightPx
                            }

                            val spannableTitle = SpannableString(pod.ofTheDayResponseData.title)
                            spannableTitle.setSpan(
                                ForegroundColorSpan(
                                    requireContext().getColorFromAttr(R.attr.colorSecondaryVariant)
                                ),
                                0,
                                pod.ofTheDayResponseData.title?.length ?: 0,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )


                            bottomSheetPodDescriptionHeader.text = spannableTitle
                            bottomSheetPodDescription.text = makeDictionaryStyledImageDescription(
                                pod.ofTheDayResponseData.explanation
                            )
//                                pod.ofTheDayResponseData.explanation


                            val spannableTimestamp = SpannableString(pod.ofTheDayResponseData.date)
                            spannableTimestamp.setSpan(
                                ForegroundColorSpan(
                                    requireContext().getColorFromAttr(R.attr.colorSecondary)
                                ),
                                0,
                                pod.ofTheDayResponseData.date?.length ?: 0,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            spannableTimestamp.setSpan(
                                UnderlineSpan(),
                                0,
                                pod.ofTheDayResponseData.date?.length ?: 0,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            bottomSheetPodDateStamp.text = spannableTimestamp
                        }
                    }
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

    private fun makeDictionaryStyledImageDescription(text: String?): CharSequence? {

        if (text.isNullOrEmpty()) {
            return null
        }
        val regexMaskBuilder = StringBuilder()

        App.wordsDictionary.keyWords().forEach { keyword ->
            if (keyword.isNotEmpty()) {
                val word = if (regexMaskBuilder.length > 0) "${keyword.toLowerCase()}|"
                else "\\s($keyword|"
                regexMaskBuilder.append(word)
            }
        }
        if (regexMaskBuilder.isNotEmpty()) {
            regexMaskBuilder.replace(regexMaskBuilder.length - 1, regexMaskBuilder.length, ")")
            regexMaskBuilder.append("\\s")
        }

        val regex = Regex(regexMaskBuilder.toString(), RegexOption.IGNORE_CASE)
        val matches = regex.find(text)

        val spannableResult = SpannableString(text)

        var match = matches
        var range: IntRange? = null

        while (match != null) {
            range = match.range
            range.let {
                spannableResult.setSpan(
                    UnderlineSpan(),
                    range.start + 1,
                    range.last,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                spannableResult.setSpan(
                    ForegroundColorSpan(
                        requireContext().getColorFromAttr(R.attr.colorSecondary)
                    ),
                    range.start + 1,
                    range.last,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            match = match.next()
        }
        return spannableResult
    }

    private fun loadImage(url: String) {
        vb?.imageView?.load(url) {
            lifecycle(this@PictureOfTheDayFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
        }
    }

    private fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vb = null
    }

    override fun backPressed(): Boolean {
        return viewModel.onBackPressed()
    }

    companion object {
        @JvmStatic
        fun newInstance(dayOffset: Int) = PictureOfTheDayFragment(dayOffset)

        private fun getPODViewModelFactory(lastDayOffset: Int): ViewModelProvider.Factory {
            return PODViewModelFactory(lastDayOffset)
        }
    }

    private class PODViewModelFactory(val lastDayOffset: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (lastDayOffset < 0) throw IllegalArgumentException("Invalid lastDayOffset parameter")
            var date: String? = null

            if (lastDayOffset > 0) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE, -lastDayOffset)
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                date = formatter.format(calendar.time)
            }
            return modelClass.getConstructor(String::class.java).newInstance(date)
        }
    }
}