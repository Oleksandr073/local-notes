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
                            notes = if (newNote.id == 0) {
                                notes + newNote.copy(id = (notes.maxOfOrNull { it.id } ?: 0) + 1)
                            } else {
                                notes.map { if (it.id == newNote.id) newNote else it }
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
        Note(1, "Shopping List", "Milk, Bread, Eggs"),
        Note(2, "Workout Plan", "Pushups, Squats, Plank")
    )
}
