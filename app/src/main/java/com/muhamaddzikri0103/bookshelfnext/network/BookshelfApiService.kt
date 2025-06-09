package com.muhamaddzikri0103.bookshelfnext.network

import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

private const val BASE_URL = "https://ebac-36-69-203-148.ngrok-free.app/"

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

    @GET("readings/{id}")
    suspend fun getReadingById(
        @Path("id") readingId: String,
        @Header("Authorization") userId: String
    ): Reading
}

object ReadingsApi {
    val service: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }