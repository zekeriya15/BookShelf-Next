package com.muhamaddzikri0103.bookshelfnext.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Readings")
data class ReadingOld(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val currentPage: Int = 0,
    val dateModified: String,
    val isDeleted: Boolean = false,
    val bookId: Long = 0L
)
