package com.muhamaddzikri0103.bookshelfnext.network

import com.muhamaddzikri0103.bookshelfnext.model.OpStatus
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://bookshelf-api.zero-dev.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BookshelfApiService {
    @GET("readings")
    suspend fun getReadings(
        @Header("Authorization") userId: String,
        @Query("is_deleted") isDeleted: String
    ): List<Reading>

    @GET("readings/{id}")
    suspend fun getReadingById(
        @Path("id") readingId: Int,
        @Header("Authorization") userId: String
    ): Reading

    @Multipart
    @PUT("readings/{id}")
    suspend fun updateCurrentPage(
        @Path("id") readingId: Int,
        @Header("Authorization") userId: String,
        @Part("currentPage") currentPage: RequestBody
    ): OpStatus

    @PATCH("readings/{id}/is-deleted")
    suspend fun updateDeletedStatus(
        @Path("id") readingId: Int,
        @Header("Authorization") userId: String,
        @Body isDeletedStatus: Map<String, Boolean>
    ): OpStatus

    @DELETE("readings/{id}")
    suspend fun deleteReadingById(
        @Path("id") readingId: Int,
        @Header("Authorization") userId: String
    ): OpStatus

    @DELETE("readings/deleted")
    suspend fun deleteSoftDeletedReadings(
        @Header("Authorization") userId: String
    ): OpStatus

    @Multipart
    @POST("readings")
    suspend fun addReading(
        @Header("Authorization") userId: String,
        @Part image: MultipartBody.Part?,
        @Part("title") title: RequestBody,
        @Part("author") author: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("pages") pages: RequestBody
    ): OpStatus

    @Multipart
    @PUT("readings/{id}")
    suspend fun updateReading(
        @Path("id") readingId: Int,
        @Header("Authorization") userId: String,
        @Part image: MultipartBody.Part?,
        @Part("title") title: RequestBody,
        @Part("author") author: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("pages") pages: RequestBody,
        @Part("currentPage") currentPage: RequestBody
    ): OpStatus
}

object ReadingsApi {
    val service: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }