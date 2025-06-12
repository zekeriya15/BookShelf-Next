package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.muhamaddzikri0103.bookshelfnext.network.ApiStatus
import com.muhamaddzikri0103.bookshelfnext.network.ReadingsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TrashViewModel : ViewModel() {

    var deletedDatas = mutableStateOf(emptyList<Reading>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveDeletedDatas(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val result = ReadingsApi.service.getReadings(
                    userId,
                    "true")
                deletedDatas.value = result
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("TrashViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun restoreData(readingId: Int, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ReadingsApi.service.updateDeletedStatus(
                    readingId = readingId,
                    userId = userId,
                    isDeletedStatus = mapOf("isDeleted" to false)
                )

                if (result.status == "success") {
                    retrieveDeletedDatas(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("TrashViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun hardDelete(readingId: Int, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ReadingsApi.service.deleteReadingById(readingId, userId)

                if (result.status == "success") {
                    retrieveDeletedDatas(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("TrashViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteAllTrash(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ReadingsApi.service.deleteSoftDeletedReadings(userId)

                if (result.status == "success") {
                    retrieveDeletedDatas(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("TrashViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}