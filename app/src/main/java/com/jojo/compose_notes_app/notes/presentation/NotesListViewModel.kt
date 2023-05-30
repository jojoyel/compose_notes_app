package com.jojo.compose_notes_app.notes.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.compose_notes_app.notes.domain.use_case.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(private val notesUseCases: NotesUseCases) :
    ViewModel() {

    var state by mutableStateOf(NotesListState())
        private set

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            notesUseCases.getNotes().collect {
                state = state.copy(notes = it)
            }
        }
    }
}