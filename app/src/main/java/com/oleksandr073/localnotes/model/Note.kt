package com.oleksandr073.localnotes.model

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),

    val title: String = "",
    val content: String = "",

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    val folderId: String = ""
)
