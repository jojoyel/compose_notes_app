package com.jojo.compose_notes_app.notes.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jojo.compose_notes_app.notes.domain.use_case.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle, private val notesUseCases: NotesUseCases): ViewModel() {

    var state by mutableStateOf(NoteDetailsState())
        private set

    init {
        val id = savedStateHandle.get<Int>("id")
        id?.let {
            if (id != -1) {
                val note = notesUseCases.getSingleNote(id)
                state = state.copy(note = note)
            }
        }
    }
}