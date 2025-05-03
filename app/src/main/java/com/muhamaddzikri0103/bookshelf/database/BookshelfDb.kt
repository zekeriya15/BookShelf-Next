package com.muhamaddzikri0103.bookshelf.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muhamaddzikri0103.bookshelf.model.Book
import com.muhamaddzikri0103.bookshelf.model.Reading

@Database(entities = [Book::class, Reading::class], version = 1, exportSchema = false)
abstract class BookshelfDb : RoomDatabase() {

    abstract val dao: BookshelfDao

    companion object {

        @Volatile
        private var INSTANCE: BookshelfDb? = null

        fun getInstance(context: Context): BookshelfDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookshelfDb::class.java,
                        "bookshelf.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}