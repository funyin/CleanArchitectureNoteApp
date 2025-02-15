package com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.use_case.NoteUseCases
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val  noteUseCases: NoteUseCases
):ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state:State<NotesState> = _state
    var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job?=null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.Order -> {
                if(state.value.noteOrder::class==event.newOrder::class&&state.value.noteOrder.orderType==event.newOrder.orderType)
                    return
                else
                    getNotes(event.newOrder)
            }
            NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote?:return@launch)
                    recentlyDeletedNote = null
                }
            }
            NotesEvent.ToggleOrderSection ->_state.value = state.value.copy(
                isOrderSectionVisible = !state.value.isOrderSectionVisible
            )
            is NotesEvent.DeleteNote -> viewModelScope.launch{
                noteUseCases.deleteNote(event.note)
                recentlyDeletedNote = event.note
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder){
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder).onEach {notes->
            _state.value = state.value.copy(notes = notes, noteOrder = noteOrder)
        }.launchIn(viewModelScope)
    }
}