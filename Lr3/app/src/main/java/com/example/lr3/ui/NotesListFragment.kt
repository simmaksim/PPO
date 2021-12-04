package com.example.lr3.ui

import android.content.res.Configuration
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.example.lr3.R
import com.example.lr3.adapters.NotesAdapter
import com.example.lr3.adapters.SwipeToDeleteCallback
import com.example.lr3.data.Note
import com.example.lr3.viewModels.NotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesListFragment : Fragment() {

    private lateinit var noteList: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var list: List<Note>
    private lateinit var newButton: FloatingActionButton
    private  lateinit var sortButton: ImageButton
    private lateinit var searchView: SearchView

    override fun onCreateView(
        layoutInflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  layoutInflater.inflate(R.layout.fragment_notes_list, container, false)


        notesViewModel = activity?.run {
            ViewModelProvider(this)[NotesViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        notesAdapter = NotesAdapter(requireContext(), notesViewModel) { position ->
            setUpDetails(position)
        }
        noteList = view.findViewById(R.id.notes_list)
        noteList.adapter = notesAdapter

        notesViewModel.notes.observe(viewLifecycleOwner, Observer { notes ->
            notes?.let {
                notesAdapter.setNotes(it.toMutableList())
                this.list = it
            }
        })

        setUpRecyclerViewLayout()

        newButton = view.findViewById(R.id.new_note_button)
        newButton.setOnClickListener {
            redirectToNewFragment()
        }

        sortButton = view.findViewById(R.id.sort_image_button)
        sortButton.setOnClickListener {
            showPopup(it)
        }
        searchView = view.findViewById(R.id.notes_search)
        searchView.setOnCloseListener {
            notesViewModel.setAllNotes()
            false
        }
        searchView.setOnQueryTextListener (object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                notesViewModel.searchTag.value = searchView.query.toString()
                notesViewModel.searchTags()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
        return view
    }

    private fun setUpRecyclerViewLayout() {
        val currentOrientation = activity?.resources?.configuration?.orientation
        if (currentOrientation != Configuration.ORIENTATION_LANDSCAPE) {
            noteList.layoutManager = LinearLayoutManager(requireContext())
            val decoration = DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
            noteList.addItemDecoration(decoration)
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(notesAdapter))
            itemTouchHelper.attachToRecyclerView(noteList)
        } else {
            noteList.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun setUpDetails(position: Int) {
        val note = list[position]
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val fragment = NoteFragment.newInstance(note, notesViewModel)
        fragmentTransaction
            ?.addToBackStack(null)
            ?.replace(R.id.fragment_container, fragment)
            ?.commit()
    }

    private fun redirectToNewFragment() {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val fragment = NoteFormFragment()
        fragmentTransaction
            ?.addToBackStack(null)
            ?.replace(R.id.fragment_container, fragment)
            ?.commit()
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.sort_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.by_title_option -> {
                    notesViewModel.rearrangeNotes(NotesViewModel.Order.BY_TITLE)
                }
                R.id.by_date_option -> {
                    notesViewModel.rearrangeNotes(NotesViewModel.Order.BY_DATE)
                }
            }
            true
        }
        popup.show()
    }
}
