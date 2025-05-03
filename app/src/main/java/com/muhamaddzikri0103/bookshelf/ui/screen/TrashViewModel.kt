package com.muhamaddzikri0103.bookshelf.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhamaddzikri0103.bookshelf.database.BookshelfDao
import com.muhamaddzikri0103.bookshelf.model.BookAndReading
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrashViewModel(private val dao: BookshelfDao) : ViewModel() {

    val data: StateFlow<List<BookAndReading>> = dao.getDeletedBookAndReading().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun restore(readingId: Long) {
        viewModelScope.launch {
            dao.restoreReading(readingId)
        }
    }

    fun delete(bookNReading: BookAndReading) {
        viewModelScope.launch {
            dao.hardDeleteBookAndReading(bookNReading)
        }
    }

    fun deleteAllTrash() {
        viewModelScope.launch {
            dao.hardDeleteAllTrash()
        }
    }
}