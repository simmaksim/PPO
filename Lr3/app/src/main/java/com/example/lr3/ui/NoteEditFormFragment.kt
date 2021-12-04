package com.example.lr3.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lr3.R
import com.example.lr3.data.Note
import com.example.lr3.viewModels.NotesViewModel
import java.time.LocalDate

class NoteEditFormFragment: Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var tagsEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var done: Button
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  layoutInflater.inflate(R.layout.fragment_note_form, container, false)
        notesViewModel = activity?.run {
            ViewModelProvider(this)[NotesViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        val note = arguments?.getParcelable<Note>(ARGS_NOTE)!!
        val tags = arguments?.getStringArrayList(ARGS_TAGS)?.toList()!!
        titleEditText = view.findViewById(R.id.form_title_edit_text)
        titleEditText.setText(note.title)
        tagsEditText = view.findViewById(R.id.form_tags_edit_text)
        if (tags.isNotEmpty()) tagsEditText.setText(tags.joinToString(" "))
        contentEditText = view.findViewById(R.id.form_content_edit_text)
        contentEditText.setText(note.content)
        done = view.findViewById(R.id.form_done_button)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    redirectToNoteFragment(note)
                }
            }
        )
        done.setOnClickListener {
            updateNote(note, tags)
            redirectToNoteFragment(note)
        }
        return view
    }

    companion object {
        private const val ARGS_NOTE = "ARGS_NOTE"
        private const val ARGS_TAGS = "ARG_TAGS"

        fun newInstance(note: Note, tags: List<String>): NoteEditFormFragment {
            val fragment = NoteEditFormFragment()
            val args = Bundle()
            args.putParcelable(ARGS_NOTE, note)
            val arrayTags = ArrayList<String>(tags)
            args.putStringArrayList(ARGS_TAGS, arrayTags)
            fragment.arguments = args
            return fragment
        }
    }
    private fun updateNote(note: Note, oldTags: List<String>) {
        val title: String = if (!TextUtils.isEmpty(titleEditText.text.toString())) {
            titleEditText.text.toString()
        } else LocalDate.now().toString()
        val tagsString: String = tagsEditText.text.toString()
        val content: String = contentEditText.text.toString()
        notesViewModel.updateNote(note, oldTags, title, tagsString, content)
    }

    private fun redirectToNoteFragment(note: Note) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val fragment = NoteFragment.newInstance(note, notesViewModel)
        fragmentManager?.popBackStack()
        fragmentTransaction
            ?.addToBackStack(null)
            ?.replace(R.id.fragment_container, fragment)
            ?.commit()
    }
}