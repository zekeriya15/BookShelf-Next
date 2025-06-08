package com.muhamaddzikri0103.bookshelfnext.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

private const val BASE_URL = "https://1ee4-36-69-203-148.ngrok-free.app/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BookshelfApiService {
    @GET("/readings")
    suspend fun getReadings(
        @Header("Authorization") userId: String
    ): String
}

object ReadingsApi {
    val service: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }
}