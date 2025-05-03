package com.muhamaddzikri0103.bookshelf.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.muhamaddzikri0103.bookshelf.database.BookshelfDao
import com.muhamaddzikri0103.bookshelf.model.Book
import com.muhamaddzikri0103.bookshelf.model.BookAndReading
import com.muhamaddzikri0103.bookshelf.model.Reading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpsertViewModel(private val dao: BookshelfDao) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(
        title: String,
        author: String,
        genre: String,
        numOfPages: String,
    ) {
        val book = Book(
            title = title,
            author = author,
            genre = genre,
            numOfPages = numOfPages.toInt()
        )
        val reading = Reading(
            dateModified = formatter.format(Date())
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertBookAndReading(book, reading)
        }
    }

    fun getBookAndReadingById(readingId: Long): Flow<BookAndReading> {
        return dao.getBookAndReadingByReadingId(readingId)
    }
}