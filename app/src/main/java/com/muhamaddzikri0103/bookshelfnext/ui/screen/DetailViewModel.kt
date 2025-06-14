package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.graphics.Bitmap
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class DetailViewModel : ViewModel() {

    var currentReading = MutableStateFlow<Reading?>(null)
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveDataById(readingId: Int, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val result = ReadingsApi.service.getReadingById(readingId, userId)

                currentReading.value = result
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("DetailViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun addPages(readingId: Int, userId: String, currentPage: Int) {
        viewModelScope.launch(Dispatchers.IO) {
          try {
              val result = ReadingsApi.service.updateCurrentPage(
                  readingId = readingId,
                  userId = userId,
                  currentPage = currentPage.toString().toRequestBody("text/plain".toMediaTypeOrNull())
              )

              if (result.status == "success") {
                  retrieveDataById(readingId, userId)
              } else {
                  throw Exception(result.message)
              }
          } catch (e: Exception) {
              Log.d("DetailViewModel", "Failure: ${e.message}")
              errorMessage.value = "Error: ${e.message}"
          }
        }
    }

    fun softDelete(readingId: Int, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ReadingsApi.service.updateDeletedStatus(
                    readingId = readingId,
                    userId = userId,
                    isDeletedStatus = mapOf("isDeleted" to true)
                )

                if (result.status == "success") {
                    retrieveDataById(readingId, userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("DetailViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateData(
        readingId: Int, userId: String,
        bitmap: Bitmap?, title: String,
        author: String, genre: String,
        pages: Int, currentPage: Int,
        deleteImage: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
                val authorBody = author.toRequestBody("text/plain".toMediaTypeOrNull())
                val genreBody = genre.toRequestBody("text/plain".toMediaTypeOrNull())
                val pagesBody = pages.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val currPageBody = currentPage.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart: MultipartBody.Part? = when {
                    bitmap != null -> {
                        // new bitmap selected
                        bitmap.toMultipartBody()
                    }

                    deleteImage -> {
                        // user wants to delete existing image
                        MultipartBody.Part.createFormData(
                            "image",
                            "",
                            "".toRequestBody("text/plain".toMediaTypeOrNull())
                        )
                    }

                    else -> {
                        // image isnt updated, image remain unchanged
                        null
                    }
                }

                val result = ReadingsApi.service.updateReading(
                    readingId = readingId,
                    userId = userId,
                    image = imagePart,
                    title = titleBody,
                    author = authorBody,
                    genre = genreBody,
                    pages = pagesBody,
                    currentPage = currPageBody
                )

                if (result.status == "success")
                    retrieveDataById(readingId, userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("DetailViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }

    fun clearMessage() { errorMessage.value = null }

}