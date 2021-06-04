package com.example.nasaapp.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ActivityNoteViewBinding
import com.example.nasaapp.model.data.INotesRepo
import com.example.nasaapp.model.data.NasaAppNote
import com.example.nasaapp.model.data.SimpleNotesRepo
import com.example.nasaapp.ui.App
import com.example.nasaapp.ui.utils.toColorResId
import com.example.nasaapp.ui.viewmodel.NasaAppNoteViewModel
import com.example.nasaapp.ui.dialogs.ColorSelectionDialog
import com.example.nasaapp.ui.utils.colorToPredefinedColor
import com.example.nasaapp.ui.utils.toColor
import java.text.SimpleDateFormat
import java.util.*

class NasaAppNoteViewActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_NOTE = "NoteViewActivity.extra.NasaAppNote"
        const val SAVE_DELAY_MS = 1000.toLong()

        fun noteViewActivityIntent(context: Context, uid: String?): Intent {
            val intent = Intent(context, NasaAppNoteViewActivity::class.java)
            intent.putExtra(EXTRA_NOTE, uid)
            return intent
        }

        private fun getNasaAppNoteViewModelFactory(repo: INotesRepo): ViewModelProvider.Factory {
            return NasaAppNoteViewActivity.NasaAppNoteViewModelFactory(repo)
        }
    }

    private var note: NasaAppNote? = null

    val viewModel: NasaAppNoteViewModel by lazy {
        ViewModelProviders.of(this, getNasaAppNoteViewModelFactory(SimpleNotesRepo))
            .get(NasaAppNoteViewModel::class.java)
    }

    private val ui: ActivityNoteViewBinding by lazy {
        ActivityNoteViewBinding.inflate(layoutInflater)
    }

    private val onTextChangedListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            saveNote()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(App.themeProvider.getCurrentThemeResourceID())
        setContentView(ui?.root)
        setupActionBar()
        val uid = intent.getStringExtra(EXTRA_NOTE)


        uid?.let { id ->
            viewModel.loadNote(id)
        }
        addOnTextChangedListener()

        viewModel.viewState().observe(this, Observer<NasaAppNote?> { note->
            renderData(note)
        })

    }

    private fun addOnTextChangedListener() {
        ui.titleEditorText.addTextChangedListener(onTextChangedListener)
        ui.bodyEditorText.addTextChangedListener(onTextChangedListener)
    }

    private fun removeOnTextChangedListener() {
        ui.titleEditorText.removeTextChangedListener(onTextChangedListener)
        ui.bodyEditorText.removeTextChangedListener(onTextChangedListener)
    }


    private fun initView() {
        note?.let { NasaAppNote ->
            supportActionBar?.apply {
                title =
                    SimpleDateFormat(getString(R.string.date_format), Locale.getDefault()).format(
                        NasaAppNote.lastChanged
                    )
                setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(applicationContext, NasaAppNote.color.toColorResId())
                    )
                )
            }
            removeOnTextChangedListener()
            ui.titleEditorText.setText(NasaAppNote.title)
            ui.bodyEditorText.setText(NasaAppNote.text)
            addOnTextChangedListener()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(ui.noteToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.new_note_title)
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(applicationContext, NasaAppNote().color.toColorResId())
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        R.id.action_color_pick -> showColorPickerDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun showColorPickerDialog() {
        val colorDialog = ColorSelectionDialog()
        colorDialog.onColorSelectedListener = { color ->
            applyNoteColor(color)
        }
        colorDialog.selectedColor =
            note?.let { it.color.toColor(this) } ?: NasaAppNote().color.toColor(this)
        colorDialog.show(supportFragmentManager, getString(R.string.color_selection_title))
    }

    fun applyNoteColor(color: Int?) {
        if (note == null) {
            note = NasaAppNote()
        }
        note?.let { NasaAppNote ->
            color?.let { color ->
                val newColor =
                    colorToPredefinedColor(applicationContext, color, NasaAppNote().color)
                NasaAppNote.color = newColor
                initView()
                saveNote()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        menuInflater.inflate(R.menu.note_editor_menu, menu).let { true }


    private fun saveNote() = ui.titleEditorText.text?.let {
        Handler(Looper.getMainLooper()).postDelayed({
            note = note?.copy(
                title = ui.titleEditorText.text.toString(),
                text = ui.bodyEditorText.text.toString(),
                lastChanged = Date()
            ) ?: NasaAppNote(
                title = ui.titleEditorText.text.toString(),
                text = ui.bodyEditorText.text.toString(),
                lastChanged = Date(),
            )
            note?.let { NasaAppNote ->
                viewModel.saveChanges(NasaAppNote)
            }
        }, SAVE_DELAY_MS)
    }

    fun renderData(data: NasaAppNote?) {
        note = data
        initView()
    }

    private class NasaAppNoteViewModelFactory(val repo: INotesRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(INotesRepo::class.java).newInstance(repo)
        }
    }
}