package com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.add_edit_notes

data class TextFieldState (
    val value:String = "",
    val hint:String = "",
    val isHintVisible:Boolean = value.isEmpty())