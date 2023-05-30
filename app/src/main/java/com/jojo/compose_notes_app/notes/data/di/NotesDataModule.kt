package com.jojo.compose_notes_app.notes.data.di

import android.app.Application
import androidx.room.Room
import com.jojo.compose_notes_app.notes.data.local.NotesDatabase
import com.jojo.compose_notes_app.notes.data.repository.NotesRepositoryImpl
import com.jojo.compose_notes_app.notes.domain.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesDataModule {

    @Provides
    @Singleton
    fun providesNotesDatabase(app: Application): NotesDatabase = Room
        .databaseBuilder(app, NotesDatabase::class.java, NotesDatabase.DATABASE_NAME)
        .build()

    @Provides
    @Singleton
    fun providesNotesRepository(db: NotesDatabase): NotesRepository = NotesRepositoryImpl(db.dao)

}