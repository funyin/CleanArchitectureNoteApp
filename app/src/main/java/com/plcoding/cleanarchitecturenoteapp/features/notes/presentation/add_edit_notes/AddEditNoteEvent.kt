package com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.add_edit_notes

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    data class TitleChanged(val value:String):AddEditNoteEvent()
    data class TitleFocusChanges(val focusState: FocusState):AddEditNoteEvent()
    data class ContentChanged(val value:String):AddEditNoteEvent()
    data class ContentFocusChanges(val focusState: FocusState):AddEditNoteEvent()
    data class ColorChangedEvent(val color:Int):AddEditNoteEvent()
    object SaveNote:AddEditNoteEvent()
}