package com.muhamaddzikri0103.bookshelf.model

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val genre: String,
    val numOfPages: Int
)
