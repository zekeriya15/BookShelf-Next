package com.muhamaddzikri0103.bookshelfnext.network

import com.muhamaddzikri0103.bookshelfnext.model.OpStatus
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://4413-36-69-203-148.ngrok-free.app/"

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
        @Header("Authorization") userId: String
    ): List<Reading>

    @GET("readings")
    suspend fun getReadings(
        @Header("Authorization") userId: String,
        @Query("is_deleted") isDeleted: String
    ): List<Reading>

    @GET("readings/{id}")
    suspend fun getReadingById(
        @Path("id") readingId: String,
        @Header("Authorization") userId: String
    ): Reading

    @PUT("readings/{id}")
    suspend fun updateCurrentPage(
        @Path("id") readingId: String,
        @Header("Authorization") userId: String,
        @Part("currentPage") currentPage: RequestBody
    ): OpStatus
}

object ReadingsApi {
    val service: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }