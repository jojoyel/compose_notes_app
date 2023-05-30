package com.jojo.compose_notes_app.notes.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.compose_notes_app.R
import com.jojo.compose_notes_app.notes.domain.model.Note
import com.jojo.compose_notes_app.notes.domain.use_case.NotesUseCases
import com.jojo.compose_notes_app.util.UiEvent
import com.jojo.compose_notes_app.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val notesUseCases: NotesUseCases
) : ViewModel() {

    var state by mutableStateOf(NoteDetailsState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var inputChangesJob: Job? = null

    init {
        try {
            val id = checkNotNull(savedStateHandle.get<Int>("id"))

            if (id != -1) {
                viewModelScope.launch {
                    val note = notesUseCases.getSingleNote(id)
                    state = state.copy(
                        originalNote = note,
                        note = Note(
                            id = note.id,
                            title = note.title,
                            content = note.content,
                            color = note.color,
                            favorite = note.favorite
                        ),
                        loading = false
                    )
                }
            } else state = state.copy(loading = false) // New note
        } catch (e: IllegalStateException) {
            state = state.copy(loading = false)
            viewModelScope.launch {
                _uiEvent.send(UiEvent.GoBack)
            }
        }
    }

    fun onEvent(event: NoteDetailsEvent) {
        when (event) {
            is NoteDetailsEvent.OnTitleChanged -> {
                val note = state.note.copy(title = event.title)
                state = state.copy(note = note)
                noteModified()
            }

            is NoteDetailsEvent.OnContentChanged -> {
                val note = state.note.copy(content = event.content)
                state = state.copy(note = note)
                noteModified()
            }

            is NoteDetailsEvent.OnFavoriteToggled -> {
                val note = state.note.copy(favorite = !state.note.favorite)
                state = state.copy(note = note)
                noteModified(true)
            }

            is NoteDetailsEvent.OnCreateNoteClicked -> {
                if (state.note.content.isBlank() && state.note.title.isBlank())
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.GoBackWithMessage(UiText.StringResource(R.string.note_not_saved)))
                    }
                else
                    viewModelScope.launch {
                        notesUseCases.insertNote(state.note)
                        _uiEvent.send(UiEvent.GoBack)
                    }
            }

            is NoteDetailsEvent.OnColorChanged -> {
                val note = state.note.copy(color = event.color)
                state = state.copy(note = note)
                noteModified(true)
            }

            NoteDetailsEvent.OnDeleteClicked -> {
                viewModelScope.launch {
                    notesUseCases.deleteNote(state.note.id!!)
                    _uiEvent.send(UiEvent.GoBackWithMessage(UiText.StringResource(R.string.note_deleted)))
                }
            }
        }
    }

    private fun noteModified(immediate: Boolean = false) {
        if (state.originalNote == null) return
        inputChangesJob?.cancel()
        inputChangesJob = viewModelScope.launch {
            if (!immediate) delay(500.milliseconds)
            notesUseCases.insertNote(state.note)
        }
    }
}