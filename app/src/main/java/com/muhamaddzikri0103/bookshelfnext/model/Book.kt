package com.muhamaddzikri0103.bookshelfnext.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val author: String,
    val genre: String,
    val numOfPages: Int
)
