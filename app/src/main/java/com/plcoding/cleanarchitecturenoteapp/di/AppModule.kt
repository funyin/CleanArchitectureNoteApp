package com.plcoding.cleanarchitecturenoteapp.di

import android.app.Application
import androidx.room.Room
import com.plcoding.cleanarchitecturenoteapp.features.notes.data.repository.NoteRepositoryImp
import com.plcoding.cleanarchitecturenoteapp.features.notes.data.source.NoteDatabase
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.repository.NoteRepository
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app:Application):NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNotesRepository(db:NoteDatabase):NoteRepository{
        return NoteRepositoryImp(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNotesUseCases(repository: NoteRepository):NoteUseCases{
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}