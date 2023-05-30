package com.jojo.compose_notes_app.notes.domain.di

import com.jojo.compose_notes_app.notes.domain.repository.NotesRepository
import com.jojo.compose_notes_app.notes.domain.use_case.DeleteNote
import com.jojo.compose_notes_app.notes.domain.use_case.GetNotes
import com.jojo.compose_notes_app.notes.domain.use_case.GetSingleNote
import com.jojo.compose_notes_app.notes.domain.use_case.InsertNote
import com.jojo.compose_notes_app.notes.domain.use_case.NotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object NotesDomainModule {
    @ViewModelScoped
    @Provides
    fun providesNotesUseCases(repository: NotesRepository): NotesUseCases {
        return NotesUseCases(
            getNotes = GetNotes(repository),
            getSingleNote = GetSingleNote(repository),
            insertNote = InsertNote(repository),
            deleteNote = DeleteNote(repository)
        )
    }
}