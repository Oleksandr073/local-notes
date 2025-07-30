package com.oleksandr073.localnotes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.oleksandr073.localnotes.model.Note
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    note: Note? = null,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var folderId by remember { mutableStateOf(note?.folderId ?: "") }
    var showValidationError by remember { mutableStateOf(false) }
    
    val titleFocusRequester = remember { FocusRequester() }
    val contentFocusRequester = remember { FocusRequester() }

    val createdAtFormatted = note?.createdAt?.let { formatDate(it) }
    val updatedAtFormatted = note?.updatedAt?.let { formatDate(it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (note == null) "New Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancel")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (title.isBlank() && content.isBlank()) {
                                showValidationError = true
                                return@IconButton
                            }
                            
                            val updatedNote = Note(
                                id = note?.id ?: UUID.randomUUID().toString(),
                                title = title.trim(),
                                content = content.trim(),
                                folderId = folderId.trim(),
                                createdAt = note?.createdAt ?: System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                            onSave(updatedNote)
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { 
                    title = it
                    showValidationError = false
                },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester),
                singleLine = true,
                isError = showValidationError && title.isBlank(),
                supportingText = {
                    if (showValidationError && title.isBlank()) {
                        Text("Title is required", color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Folder Field
            OutlinedTextField(
                value = folderId,
                onValueChange = { folderId = it },
                label = { Text("Folder") },
                leadingIcon = { Icon(Icons.Default.Create, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("e.g., Work, Personal, Ideas") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content Field
            OutlinedTextField(
                value = content,
                onValueChange = { 
                    content = it
                    showValidationError = false
                },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .focusRequester(contentFocusRequester),
                maxLines = Int.MAX_VALUE,
                isError = showValidationError && content.isBlank(),
                supportingText = {
                    if (showValidationError && content.isBlank()) {
                        Text("Content is required", color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            // Metadata Section
            if (createdAtFormatted != null || updatedAtFormatted != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Note Details",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Created",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = createdAtFormatted ?: "-",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            
                            Column {
                                Text(
                                    text = "Updated",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = updatedAtFormatted ?: "-",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Auto-focus title field when creating new note
    LaunchedEffect(Unit) {
        if (note == null) {
            titleFocusRequester.requestFocus()
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
