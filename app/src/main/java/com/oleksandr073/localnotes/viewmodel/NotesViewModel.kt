package com.oleksandr073.localnotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.oleksandr073.localnotes.model.Note
import com.oleksandr073.localnotes.storage.NoteStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val filteredNotes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val editingNote: Note? = null,
    val isEditing: Boolean = false
)

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val noteStorage = NoteStorage(application)
    
    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                noteStorage.loadNotes().collect { notes ->
                    _uiState.update { state ->
                        state.copy(
                            notes = notes,
                            filteredNotes = filterNotes(notes, state.searchQuery),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = "Failed to load notes: ${e.message}"
                    ) 
                }
            }
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            try {
                val currentNotes = _uiState.value.notes.toMutableList()
                val existingIndex = currentNotes.indexOfFirst { it.id == note.id }
                
                if (existingIndex != -1) {
                    currentNotes[existingIndex] = note
                } else {
                    currentNotes.add(note)
                }
                
                noteStorage.saveNotes(currentNotes)
                _uiState.update { 
                    it.copy(
                        notes = currentNotes,
                        filteredNotes = filterNotes(currentNotes, it.searchQuery),
                        isEditing = false,
                        editingNote = null
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = "Failed to save note: ${e.message}") 
                }
            }
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            try {
                val currentNotes = _uiState.value.notes.filter { it.id != noteId }
                noteStorage.saveNotes(currentNotes)
                _uiState.update { 
                    it.copy(
                        notes = currentNotes,
                        filteredNotes = filterNotes(currentNotes, it.searchQuery)
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = "Failed to delete note: ${e.message}") 
                }
            }
        }
    }

    fun startEditing(note: Note?) {
        _uiState.update { 
            it.copy(
                editingNote = note,
                isEditing = true
            ) 
        }
    }

    fun cancelEditing() {
        _uiState.update { 
            it.copy(
                isEditing = false,
                editingNote = null
            ) 
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredNotes = filterNotes(state.notes, query)
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun filterNotes(notes: List<Note>, query: String): List<Note> {
        if (query.isBlank()) return notes
        
        return notes.filter { note ->
            note.title.contains(query, ignoreCase = true) ||
            note.content.contains(query, ignoreCase = true) ||
            note.folderId.contains(query, ignoreCase = true)
        }
    }
} 