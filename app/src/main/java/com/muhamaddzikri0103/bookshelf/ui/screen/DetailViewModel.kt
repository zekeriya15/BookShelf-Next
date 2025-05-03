package com.muhamaddzikri0103.bookshelf.ui.screen

import androidx.lifecycle.ViewModel
import com.muhamaddzikri0103.bookshelf.database.BookshelfDao
import com.muhamaddzikri0103.bookshelf.model.BookAndReading
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val dao: BookshelfDao) : ViewModel() {

    fun getBookAndReadingById(readingId: Long): Flow<BookAndReading> {
        return dao.getBookAndReadingByReadingId(readingId)
    }

}