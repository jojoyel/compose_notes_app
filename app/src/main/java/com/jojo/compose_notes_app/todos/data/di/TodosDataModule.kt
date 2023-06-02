package com.jojo.compose_notes_app.todos.data.di

import com.jojo.compose_notes_app.notes.data.local.NotesDatabase
import com.jojo.compose_notes_app.todos.data.repository.TodosRepositoryImpl
import com.jojo.compose_notes_app.todos.domain.repository.TodosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodosDataModule {

    @Provides
    @Singleton
    fun providesTodosRepository(db: NotesDatabase): TodosRepository =
        TodosRepositoryImpl(dao = db.todosDao)
}