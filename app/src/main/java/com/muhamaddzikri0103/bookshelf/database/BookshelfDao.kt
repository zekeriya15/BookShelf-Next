package com.muhamaddzikri0103.bookshelf.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.muhamaddzikri0103.bookshelf.model.Book
import com.muhamaddzikri0103.bookshelf.model.BookAndReading
import com.muhamaddzikri0103.bookshelf.model.Reading
import kotlinx.coroutines.flow.Flow

@Dao
interface BookshelfDao {

    @Insert
    suspend fun insertBook(book: Book): Long

    @Insert
    suspend fun insertReading(reading: Reading)

    @Update
    suspend fun updateBook(book: Book)

    @Update
    suspend fun updateReading(reading: Reading)

    @Transaction
    suspend fun insertBookAndReading(book: Book, reading: Reading) {
        val bookId = insertBook(book)

        val readingWithBookId = reading.copy(bookId = bookId)

        insertReading(readingWithBookId)
    }

    @Transaction
    suspend fun updateBookAndReading(book: Book, reading: Reading) {
        updateBook(book)
        updateReading(reading)
    }

    @Query("UPDATE readings SET isDeleted = 1 WHERE id = :readingId")
    suspend fun softDeleteReading(readingId: Long)

    @Query("DELETE FROM readings WHERE id = :readingId")
    suspend fun hardDeleteReading(readingId: Long)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBook(id: Long)

    @Transaction
    suspend fun hardDeleteBookAndReading(bookAndReading: BookAndReading) {
        hardDeleteReading(bookAndReading.readingId)
        deleteBook(bookAndReading.bookId)
    }

    @Query("""
    SELECT 
        books.id AS bookId,
        books.title,
        books.author,
        books.genre,
        books.numOfPages,
        readings.id AS readingId,
        readings.currentPage,
        readings.dateModified,
        readings.isDeleted
    FROM books
    INNER JOIN readings ON books.id = readings.bookId
    WHERE readings.isDeleted = 0
    ORDER BY readings.dateModified DESC
     """)
    fun getBookAndReading(): Flow<List<BookAndReading>>

    @Query("UPDATE readings SET isDeleted = 0 WHERE id = :readingId")
    suspend fun restoreReading(readingId: Long)

    @Query("""
    SELECT 
        books.id AS bookId,
        books.title,
        books.author,
        books.genre,
        books.numOfPages,
        readings.id AS readingId,
        readings.currentPage,
        readings.dateModified,
        readings.isDeleted
    FROM books
    INNER JOIN readings ON books.id = readings.bookId
    WHERE readings.isDeleted = 1
    ORDER BY readings.dateModified DESC
    """)
    fun getDeletedBookAndReading(): Flow<List<BookAndReading>>

    @Query("""
    SELECT 
        books.id AS bookId,
        books.title,
        books.author,
        books.genre,
        books.numOfPages,
        readings.id AS readingId,
        readings.currentPage,
        readings.dateModified,
        readings.isDeleted
    FROM books
    INNER JOIN readings ON books.id = readings.bookId
    WHERE readings.id = :readingId
    ORDER BY readings.dateModified DESC
     """)
    fun getBookAndReadingByReadingId(readingId: Long): Flow<BookAndReading>

    @Query("SELECT bookId FROM readings WHERE isDeleted = 1")
    suspend fun getDeletedReadingsBookIds(): List<Long>

    @Query("DELETE FROM readings WHERE isDeleted = 1")
    suspend fun deleteAllDeletedReadings()

    @Query("DELETE FROM books WHERE id IN (:bookIds)")
    suspend fun deleteBooksByIds(bookIds: List<Long>)

    @Transaction
    suspend fun hardDeleteAllTrash() {
        val bookIds = getDeletedReadingsBookIds()

        deleteAllDeletedReadings()

        if (bookIds.isNotEmpty()) {
            deleteBooksByIds(bookIds)
        }
    }

}