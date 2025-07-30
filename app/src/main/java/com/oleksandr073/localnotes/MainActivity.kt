package com.oleksandr073.localnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.oleksandr073.localnotes.ui.NoteEditorScreen
import com.oleksandr073.localnotes.ui.NoteListScreen
import com.oleksandr073.localnotes.ui.components.ErrorSnackbar
import com.oleksandr073.localnotes.ui.theme.LocalNotesTheme
import com.oleksandr073.localnotes.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: NotesViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalNotesTheme {
                val uiState by viewModel.uiState.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->
                    ErrorSnackbar(
                        error = uiState.error,
                        snackbarHostState = snackbarHostState,
                        onErrorShown = { viewModel.clearError() }
                    )

                    if (uiState.isEditing) {
                        NoteEditorScreen(
                            note = uiState.editingNote,
                            onSave = { note ->
                                viewModel.saveNote(note)
                            },
                            onCancel = {
                                viewModel.cancelEditing()
                            }
                        )
                    } else {
                        Box(modifier = androidx.compose.ui.Modifier.padding(padding)) {
                            NoteListScreen(
                                notes = uiState.filteredNotes,
                                searchQuery = uiState.searchQuery,
                                isLoading = uiState.isLoading,
                                onNoteClick = { note ->
                                    viewModel.startEditing(note)
                                },
                                onAddClick = {
                                    viewModel.startEditing(null)
                                },
                                onSearchQueryChange = { query ->
                                    viewModel.updateSearchQuery(query)
                                },
                                onDeleteNote = { noteId ->
                                    viewModel.deleteNote(noteId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
