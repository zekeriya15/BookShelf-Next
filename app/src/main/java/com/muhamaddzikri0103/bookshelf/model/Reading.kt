package com.muhamaddzikri0103.bookshelf.model

data class Reading(
    val id: Long,
    val currentPage: Int,
    val isDeleted: Boolean,
    val bookId: Long
)
