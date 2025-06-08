package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhamaddzikri0103.bookshelfnext.database.BookshelfDao
import com.muhamaddzikri0103.bookshelfnext.model.Book
import com.muhamaddzikri0103.bookshelfnext.model.BookAndReading
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.muhamaddzikri0103.bookshelfnext.model.ReadingOld
import com.muhamaddzikri0103.bookshelfnext.network.ReadingsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpsertViewModel() : ViewModel() {

    private val _currentReading = MutableStateFlow<Reading?>(null)
    val currentReading: StateFlow<Reading?> = _currentReading

    fun retrieveDataById(readingId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ReadingsApi.service.getReadingById(readingId = readingId.toString(),"__admin__")
                Log.d("MainViewModel", "Success $result")
                _currentReading.value = result
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

//    fun insert(
//        title: String,
//        author: String,
//        genre: String,
//        numOfPages: String,
//    ) {
//        val book = Book(
//            title = title,
//            author = author,
//            genre = genre,
//            numOfPages = numOfPages.toInt()
//        )
//        val reading = ReadingOld(
//            dateModified = formatter.format(Date())
//        )
//        viewModelScope.launch(Dispatchers.IO) {
//            dao.insertBookAndReading(book, reading)
//        }
//    }
//
//    fun getBookAndReadingById(readingId: Long): Flow<BookAndReading> {
//        return dao.getBookAndReadingByReadingId(readingId)
//    }
//
//    fun update(
//        bookId: Long,
//        title: String,
//        author: String,
//        genre: String,
//        numOfPages: String,
//        readingId: Long,
//        currentPage: String,
//        dateModified: String = formatter.format(Date())
//    ) {
//        val book = Book(
//            id = bookId,
//            title = title,
//            author = author,
//            genre = genre,
//            numOfPages = numOfPages.toInt()
//        )
//        val reading = ReadingOld(
//            id = readingId,
//            bookId = bookId,
//            currentPage = currentPage.toIntOrNull() ?: 0,
//            dateModified = dateModified
//        )
//
//        viewModelScope.launch(Dispatchers.IO) {
//            dao.updateBookAndReading(book, reading)
//        }
//    }
//
//    fun softDelete(readingId: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            dao.softDeleteReading(readingId)
//        }
//    }

}