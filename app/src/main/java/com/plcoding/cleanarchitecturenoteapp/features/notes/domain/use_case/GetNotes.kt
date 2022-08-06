package com.plcoding.cleanarchitecturenoteapp.features.notes.domain.use_case

import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.repository.NoteRepository
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(private val repository: NoteRepository) {
    operator fun invoke(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when (noteOrder.orderType) {
                OrderType.Ascending -> when (noteOrder) {
                    is NoteOrder.Color -> notes.sortedBy { it.color }
                    is NoteOrder.Date -> notes.sortedBy { it.timeStamp }
                    is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                }
                OrderType.Descending -> when (noteOrder) {
                    is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    is NoteOrder.Date -> notes.sortedByDescending { it.timeStamp }
                    is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                }
            }
        }
    }
}