package com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.util

sealed class Screen(val route:String){
    object NotesScreen:Screen("notes_screen")
    object AddEditNoteScreen:Screen("add_edit_notes_screen")
}
