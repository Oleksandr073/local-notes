package com.oleksandr073.localnotes.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oleksandr073.localnotes.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "notes_datastore")

class NoteStorage(private val context: Context) {
    private val gson = Gson()
    private val notesKey = stringPreferencesKey("notes")

    fun loadNotes(): Flow<List<Note>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[notesKey] ?: return@map emptyList()
            val type = object : TypeToken<List<Note>>() {}.type
            gson.fromJson(json, type)
        }
    }

    suspend fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)
        context.dataStore.edit { preferences ->
            preferences[notesKey] = json
        }
    }
}
