package com.oleksandr073.localnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.oleksandr073.localnotes.model.Note
import com.oleksandr073.localnotes.ui.NoteEditorScreen
import com.oleksandr073.localnotes.ui.NoteListScreen
import com.oleksandr073.localnotes.ui.theme.LocalNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalNotesTheme {
                var notes by remember { mutableStateOf(sampleNotes()) }
                var editingNote by remember { mutableStateOf<Note?>(null) }
                var isEditing by remember { mutableStateOf(false) }

                if (isEditing) {
                    NoteEditorScreen(
                        note = editingNote,
                        onSave = { newNote ->
                            val now = System.currentTimeMillis()

                            val noteToSave = if (notes.any { it.id == newNote.id }) {
                                newNote.copy(updatedAt = now)
                            } else {
                                newNote.copy(
                                    createdAt = now,
                                    updatedAt = now
                                )
                            }

                            notes = if (notes.any { it.id == noteToSave.id }) {
                                notes.map { if (it.id == noteToSave.id) noteToSave else it }
                            } else {
                                notes + noteToSave
                            }

                            isEditing = false
                            editingNote = null
                        },
                        onCancel = {
                            isEditing = false
                            editingNote = null
                        }
                    )
                } else {
                    NoteListScreen(
                        notes = notes,
                        onNoteClick = {
                            editingNote = it
                            isEditing = true
                        },
                        onAddClick = {
                            editingNote = null
                            isEditing = true
                        }
                    )
                }
            }
        }
    }

    private fun sampleNotes() = listOf(
        Note(
            title = "Shopping List",
            content = "Milk, Bread, Eggs",
            folderId = "home"
        ),
        Note(
            title = "Workout Plan",
            content = "Pushups, Squats, Plank",
            folderId = "fitness"
        )
    )
}
