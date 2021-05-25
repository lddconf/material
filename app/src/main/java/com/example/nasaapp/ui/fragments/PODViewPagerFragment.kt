package com.example.nasaapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ViewPagerPodLayoutBinding
import com.example.nasaapp.model.PictureOfTheDayData
import com.example.nasaapp.ui.adapters.SimpleTabSPAdapter
import com.example.nasaapp.ui.viewmodel.PODViewPagerViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PODViewPagerFragment(val counter: Int = 5) : Fragment(), IBackPressableFragment {
    private var vb: ViewPagerPodLayoutBinding? = null
    private var podTabFSAdapter: SimpleTabSPAdapter? = null
    private var tabLayoutMediator: TabLayoutMediator? = null

    private val viewModel: PODViewPagerViewModel by lazy {
        ViewModelProviders.of(this).get(
            PODViewPagerViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ViewPagerPodLayoutBinding.inflate(inflater, container, false).also {
        vb = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { pod ->
            renderData(pod)
        })
        initViewPager()
        initAppBar()
        initWikiSearch()

    }

    override fun onDestroy() {
        super.onDestroy()
        vb = null
    }

    private fun initViewPager() {
        podTabFSAdapter = SimpleTabSPAdapter(this, counter)
        tabLayoutMediator?.detach()
        vb?.apply {
            pager?.adapter = podTabFSAdapter
            tabLayoutMediator = TabLayoutMediator(tabLayout, pager, true ) { tab, position -> }
            tabLayoutMediator?.attach()
        }
    }

    private fun initAppBar() {
        setHasOptionsMenu(true)
        vb?.toolbar?.inflateMenu(R.menu.picture_of_the_day_menu)
        vb?.toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    viewModel.inWikiSearchRequested()
                    true
                }
                R.id.action_settings -> {
                    viewModel.appSettingsRequested()
                    true
                }
                else -> false
            }
        }
    }

    private fun renderData(pod: PictureOfTheDayData) {
        when (pod) {
            is PictureOfTheDayData.Error -> {
                showMessage(pod.error.message)
            }
            is PictureOfTheDayData.PerformWikiSearch -> {
                startWikiSearch(pod.url)
            }
            is PictureOfTheDayData.Settings -> {
                findNavController().navigate(R.id.action_pod_settings)
            }
        }
    }

    private fun initWikiSearch() {
        viewModel.getWikiSearchMode()
            .observe(viewLifecycleOwner, Observer<Boolean> { isWikiSearchMode ->
                if (isWikiSearchMode.not()) {
                    showWikiSearch()
                } else {
                    hideWikiSearch()
                }
            })

        vb?.inputEditText?.setOnKeyListener { v, keyCode, event ->
            return@setOnKeyListener if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                vb?.inputEditText?.let { edit ->
                    if (!edit.text.isNullOrEmpty()) {
                        viewModel.performWikiSearch(edit.text.toString())
                    } else {
                        viewModel.disableWikiSearchMode()
                    }
                }
                true
            } else {
                false
            }
        }
    }

    private fun startWikiSearch(text: String) {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://en.wikipedia.org/wiki/$text")
        })
    }

    private fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showWikiSearch() {
        vb?.appbarTitle?.visibility = View.VISIBLE
        vb?.inputTextLayout?.visibility = View.GONE
        vb?.inputEditText?.text?.clear()
//        vb?.inputEditText?.requestFocus()
    }

    private fun hideWikiSearch() {
        vb?.appbarTitle?.visibility = View.GONE
        vb?.inputTextLayout?.visibility = View.VISIBLE
    }

    override fun backPressed(): Boolean {
        return viewModel.onBackPressed()
    }
}