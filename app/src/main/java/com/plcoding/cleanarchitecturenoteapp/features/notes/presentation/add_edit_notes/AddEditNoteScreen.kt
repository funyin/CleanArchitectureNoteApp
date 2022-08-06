package com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.add_edit_notes

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.cleanarchitecturenoteapp.features.notes.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.add_edit_notes.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val scaffoldState = rememberScaffoldState()
    var noteBackgroundColor by remember { mutableStateOf(Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                AddEditNoteViewModel.UIEvent.SaveNote ->
                    navController.navigateUp()
                is AddEditNoteViewModel.UIEvent.ShowSnackbarEvent -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(it.message)
                    }
                }
            }
        }
    }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.onEvent(AddEditNoteEvent.SaveNote)
        },
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
        }
    },
        scaffoldState = scaffoldState,
        backgroundColor = animateColorAsState(targetValue = noteBackgroundColor).value) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Note.colors.forEach { color ->
                    Box(modifier = Modifier
                        .size(50.dp)
                        .shadow(15.dp, shape = CircleShape)
                        .background(color = color, shape = CircleShape)
                        .border(3.dp,
                            color = if (noteBackgroundColor == color) Color.Black else Color.Transparent,
                            shape = CircleShape)
                        .clickable {
                            scope.launch {
                                noteBackgroundColor = color
                            }
                            viewModel.onEvent(AddEditNoteEvent.ColorChangedEvent(color.toArgb()))
                        }
                    ) {
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.value,
                hint = titleState.hint,
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
                onFocusChanged = { viewModel.onEvent(AddEditNoteEvent.TitleFocusChanges(it)) }) {
                viewModel.onEvent(AddEditNoteEvent.TitleChanged(it))
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                modifier = Modifier.fillMaxSize(),
                text = contentState.value,
                hint = contentState.hint,
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
                onFocusChanged = { viewModel.onEvent(AddEditNoteEvent.ContentFocusChanges(it)) }) {
                viewModel.onEvent(AddEditNoteEvent.ContentChanged(it))
            }
        }
    }
}