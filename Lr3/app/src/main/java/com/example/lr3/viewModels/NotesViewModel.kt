package com.example.lr3.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.lr3.data.*
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {

    private val folder: NotesRepository
    private val notesTitle: LiveData<List<Note>>
    private val notesDate: LiveData<List<Note>>
    private val filtered: LiveData<List<Note>>
    var notes = MediatorLiveData<List<Note>>()
    var searchTag = MutableLiveData<String>()
    val tag = "NotesViewModel"
    enum class Order { BY_TITLE, BY_DATE }
    private var currentOrder: Order


    init {
        Log.d(tag, "DB was not created")
        val db = NotesDatabase.getDatabase(application, viewModelScope)
        Log.d(tag, "DB was created")
        val noteDataDao = db.run { noteDataDao() }
        val noteTagDao = db.run { noteTagDao() }

        folder = NotesRepository(noteDataDao, noteTagDao)
        notesTitle = folder.notesByTitle
        notesDate = folder.notesByDate
        Transformations.switchMap(
            searchTag,
            { folder.getNotesByTag(it) }
        ).also { filtered = it }
        currentOrder = Order.BY_DATE

        notes.addSource(notesTitle) { result ->
            if (currentOrder == Order.BY_TITLE) {
                result?.let { notes.value = it }
            }
        }
        notes.addSource(notesDate) { result ->
            if (currentOrder == Order.BY_DATE) {
                result?.let { notes.value = it }
            }
        }
        notes.addSource(filtered) { }
    }

    fun insertNote(note: Note, tagsString: String) = viewModelScope.launch {
        val tagsList = separateTags(tagsString).map { tagStr -> Tag(tagStr) }
        folder.insertNote(note, tagsList)
    }

    fun fetchTagsFromNoteAsync(note: Note): List<Tag> = folder.getTagsFromNote(note.id)


    fun deleteNote (note: Note) = viewModelScope.launch {
        folder.deleteNote(note)
    }

    fun updateNote(
        note: Note,
        oldTags: List<String>,
        newTitle: String,
        newTagsString: String,
        newContent: String
    ) {
        val oldTagsSet = oldTags.toSet()
        val newTagsSet = separateTags(newTagsString).toSet()
        val tagsToDetach = (oldTagsSet subtract newTagsSet).toList()
        val tagsToAttach = (newTagsSet subtract oldTagsSet).toList()
        folder.updateNote(note, newTitle, newContent, tagsToDetach, tagsToAttach)
    }

    private fun separateTags(tagsString: String): List<String> {
        return tagsString
            .split("\\s+".toRegex())
            .flatMap {tag -> tag.split("#")}
            .filter { tag -> tag != "" }
    }

    fun setAllNotes() {
        if (currentOrder == Order.BY_TITLE) notesTitle.value?.let { notes.value = it } else {
            notesDate.value?.let { notes.value = it }
        }
    }

    fun rearrangeNotes(order: Order) = when (order) {
        Order.BY_TITLE -> notesTitle.value?.let { notes.value = it }
        Order.BY_DATE -> notesDate.value?.let { notes.value = it }
    }.also { currentOrder = order }

    fun searchTags() {
        if (searchTag.value == "")
            setAllNotes()
        else filtered.value?.let { notes.value = it }
    }
}