package com.muhamaddzikri0103.bookshelfnext.model

data class BookAndReading(
    val bookId: Long,
    val title: String,
    val author: String,
    val genre: String,
    val numOfPages: Int,

    val readingId: Long,
    val currentPage: Int,
    val dateModified: String,
    val isDeleted: Boolean
)
