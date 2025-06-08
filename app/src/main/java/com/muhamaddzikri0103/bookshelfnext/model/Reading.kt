package com.muhamaddzikri0103.bookshelfnext.model

data class Reading(
    val id: Int,
    val imageUrl: String?,
    val title: String,
    val author: String,
    val genre: String,
    val pages: Int,
    val currentPage: Int,
    val dateModified: String,
    val isDeleted: Boolean,
)
