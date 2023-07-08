package com.jojo.compose_notes_app.notes.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.compose_notes_app.notes.domain.model.Note
import com.jojo.compose_notes_app.notes.domain.use_case.NotesFilter
import com.jojo.compose_notes_app.notes.domain.use_case.NotesUseCases
import com.jojo.compose_notes_app.util.SelectableColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(private val notesUseCases: NotesUseCases) :
    ViewModel() {

    var state by mutableStateOf(NotesListState())
        private set

    var notes = emptyList<Note>()

    init {
        getNotes(NotesFilter.Descending)
    }

    fun onEvent(event: NotesListEvent) {
        when (event) {
            is NotesListEvent.OnColorFilterChanged -> {
                when (event.color) {
                    null -> state = state.copy(notes = notes)
                    else -> state =
                        state.copy(notes = notes.filter { SelectableColors.getColorFromHex(it.color) == event.color })
                }
            }
        }
    }

    private fun getNotes(filters: NotesFilter) {
        viewModelScope.launch {
            notesUseCases.getNotes().collect {
                notes = it
                state = state.copy(notes = it, hasNotes = it.isNotEmpty())
            }
        }
    }
}