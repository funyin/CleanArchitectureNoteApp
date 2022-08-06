package com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.add_edit_notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.model.InvalidNoteException
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
    ) :ViewModel(){

    init {
        savedStateHandle.get<Int>("noteId")?.let {noteId->
            if(noteId!=-1){
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also {note->
                        currentNoteId = noteId
                        _noteTitle.value = _noteTitle.value.copy(value = note.title, isHintVisible = false)
                        _noteContent.value = _noteContent.value.copy(value = note.content, isHintVisible = false)
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }
    private val _noteTitle = mutableStateOf<TextFieldState>(TextFieldState(hint = "Title"))
    val noteTitle: State<TextFieldState> = _noteTitle
    private val _noteContent = mutableStateOf<TextFieldState>(TextFieldState(hint = "Content"))
    val noteContent: State<TextFieldState> = _noteContent

    private val _noteColor = mutableStateOf<Int>(Note.colors.random().toArgb())
    val noteColor:State<Int> = _noteColor

    private var currentNoteId:Int? = null

    private val _eventFlow = MutableSharedFlow<UIEvent>()

    val eventFlow:SharedFlow<UIEvent> = _eventFlow

    sealed class UIEvent {
        data class ShowSnackbarEvent(var message:String):UIEvent()
        object SaveNote:UIEvent()
    }

    fun onEvent(event:AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.ColorChangedEvent -> _noteColor.value = event.color
            is AddEditNoteEvent.ContentChanged -> _noteContent.value= _noteContent.value.copy(value = event.value,isHintVisible = event.value.isEmpty())
            is AddEditNoteEvent.ContentFocusChanges -> _noteContent.value= _noteContent.value.copy(isHintVisible = !event.focusState.hasFocus && _noteContent.value.value.isEmpty())
            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.value,
                                content = noteContent.value.value,
                                timeStamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UIEvent.SaveNote)
                    }catch (e:InvalidNoteException){
                        _eventFlow.emit(UIEvent.ShowSnackbarEvent(e.message))
                    }
                }
            }
            is AddEditNoteEvent.TitleChanged -> _noteTitle.value= _noteTitle.value.copy(value = event.value, isHintVisible = event.value.isEmpty())
            is AddEditNoteEvent.TitleFocusChanges -> _noteTitle.value= _noteTitle.value.copy(isHintVisible = !event.focusState.hasFocus&&_noteTitle.value.value.isEmpty())
        }
    }

}
