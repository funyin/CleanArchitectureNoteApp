package com.plcoding.cleanarchitecturenoteapp.features.notes.domain.use_case

import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.model.InvalidNoteException
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.repository.NoteRepository

class AddNote (private val repository: NoteRepository){
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if(note.title.isBlank())
            throw InvalidNoteException("The title of the note cannot be empty")
        if(note.content.isBlank())
            throw InvalidNoteException("the content of the note cannot be empty")
        repository.insertNote(note)
    }
}