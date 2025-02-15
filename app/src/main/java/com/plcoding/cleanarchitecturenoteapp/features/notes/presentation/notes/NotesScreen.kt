package com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.notes.components.NoteItem
import com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.notes.components.OrderSection
import com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddEditNoteScreen.route)
            }, backgroundColor = MaterialTheme.colors.primary) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Notes",
                    style = MaterialTheme.typography.h4
                )
                IconButton(onClick = {
                    viewModel.onEvent(NotesEvent.ToggleOrderSection)
                }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                }
            }
            AnimatedVisibility(visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(modifier= Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), noteOrder = state.noteOrder){
                    viewModel.onEvent(NotesEvent.Order(it))
                }
            }
            Spacer(modifier=Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(vertical = 8.dp)){
                items(state.notes){note->
                    NoteItem(modifier= Modifier
                        .fillMaxWidth()
                        .clickable {
                                   navController.navigate(
                                       Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
                                   )
                        },note = note) {
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                        scope.launch {
                            val result =scaffoldState.snackbarHostState.
                            showSnackbar(message = "Note Deleted", actionLabel = "Undo")
                            if(result==SnackbarResult.ActionPerformed)
                                viewModel.onEvent(NotesEvent.RestoreNote)
                        }
                    }
                }
            }
        }
    }
}