package com.oleksandr073.localnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.oleksandr073.localnotes.model.Note
import com.oleksandr073.localnotes.ui.NoteListScreen
import com.oleksandr073.localnotes.ui.theme.LocalNotesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalNotesTheme {
                var notes by remember { mutableStateOf(sampleNotes()) }

                NoteListScreen(
                    notes = notes,
                    onNoteClick = { /* TODO: Open editor */ },
                    onAddClick = {
                        notes = notes + Note(
                            id = notes.size + 1,
                            title = "Note ${notes.size + 1}",
                            content = "Some content"
                        )
                    }
                )
            }
        }
    }

    private fun sampleNotes() = listOf(
        Note(1, "Shopping List", "Milk, Bread, Eggs"),
        Note(2, "Workout Plan", "Pushups, Squats, Plank")
    )
}
