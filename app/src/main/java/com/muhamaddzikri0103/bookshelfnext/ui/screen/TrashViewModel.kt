package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhamaddzikri0103.bookshelfnext.database.BookshelfDao
import com.muhamaddzikri0103.bookshelfnext.model.BookAndReading
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.muhamaddzikri0103.bookshelfnext.network.ApiStatus
import com.muhamaddzikri0103.bookshelfnext.network.ReadingsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrashViewModel() : ViewModel() {

    var deletedDatas = mutableStateOf(emptyList<Reading>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrieveDeletedDatas()
    }

    fun retrieveDeletedDatas() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val result = ReadingsApi.service.getReadings(
                    "yakup15@gmail.com",
                    "true")
//                Log.d("MainViewModel", "Success $result")
                deletedDatas.value = result
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

//    val data: StateFlow<List<BookAndReading>> = dao.getDeletedBookAndReading().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(),
//        initialValue = emptyList()
//    )
//
//    fun restore(readingId: Long) {
//        viewModelScope.launch {
//            dao.restoreReading(readingId)
//        }
//    }
//
//    fun delete(bookNReading: BookAndReading) {
//        viewModelScope.launch {
//            dao.hardDeleteBookAndReading(bookNReading)
//        }
//    }
//
//    fun deleteAllTrash() {
//        viewModelScope.launch {
//            dao.hardDeleteAllTrash()
//        }
//    }
}