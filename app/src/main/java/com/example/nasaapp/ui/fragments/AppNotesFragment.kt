package com.example.nasaapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasaapp.R
import com.example.nasaapp.databinding.AppNotesFragmentBinding
import com.example.nasaapp.model.data.INotesRepo
import com.example.nasaapp.model.data.NasaAppNote
import com.example.nasaapp.model.data.SimpleNotesRepo
import com.example.nasaapp.ui.activities.NasaAppNoteViewActivity
import com.example.nasaapp.ui.adapters.NotesRVAdapter
import com.example.nasaapp.ui.adapters.OnItemActionListener
import com.example.nasaapp.ui.viewmodel.AppNotesViewModel
import com.google.android.material.snackbar.Snackbar

class AppNotesFragment : Fragment() {
    companion object {
        fun newInstance() = AppNotesFragment()
    }

    private lateinit var viewModel: AppNotesViewModel
    private lateinit var adapter: NotesRVAdapter

    private var vb: AppNotesFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = AppNotesFragmentBinding.inflate(layoutInflater, container, false).also {
        vb = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAppBar()
        initNotesRV()
        initViewModel()
    }


    private class AppNotesViewModelFactory(val repo: INotesRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(INotesRepo::class.java).newInstance(repo)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            AppNotesViewModelFactory(SimpleNotesRepo)
        ).get(AppNotesViewModel::class.java)
        viewModel.viewState().observe(viewLifecycleOwner, Observer<List<NasaAppNote>> {
            updateNoteList(it)
        })
    }

    private fun initAppBar() {
        setHasOptionsMenu(true)
        vb?.toolbar?.inflateMenu(R.menu.notes_list_menu)
        vb?.toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
//                android.R.id.home->
//                    onBackPressed()
//                    true
                R.id.action_add -> {
                    startNoteEditor()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it);
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vb = null
    }

    private fun initNotesRV() {
        vb?.notesBriefList?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = NotesRVAdapter()
        vb?.notesBriefList?.adapter = adapter


        val swipeItemTouchHelper =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_forever_24)
                ?.let { icon ->
                    ItemTouchHelper(
                        adapter.NoteRVSwipeToDelete(
                            adapter, icon
                        )
                    )
                }

        swipeItemTouchHelper?.attachToRecyclerView(vb?.notesBriefList)

        adapter.onItemActionListener = object : OnItemActionListener {
            override fun onItemClick(note: NasaAppNote) {
                startNoteEditor(note)
            }

            override fun onItemDelete(note: NasaAppNote) {
                viewModel.deleteNote(note.uid)
                Snackbar.make(
                    vb?.notesBriefList as View,
                    getString(R.string.action_recover_deleted_note),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(getString(R.string.snckbar_recover_btn)) {
                        viewModel.undoLastDeletedNote()
                    }
                    .show()
            }
        }
    }

    private fun startNoteEditor(note: NasaAppNote? = null) {
        startActivity(NasaAppNoteViewActivity.noteViewActivityIntent(requireContext(), note?.uid))
    }

    private fun updateNoteList(notes: List<NasaAppNote>?) {
        notes?.apply {
            adapter.setNotes(this)
        }
    }
}