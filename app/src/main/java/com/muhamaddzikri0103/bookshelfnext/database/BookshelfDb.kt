package com.muhamaddzikri0103.bookshelfnext.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muhamaddzikri0103.bookshelfnext.model.Book
import com.muhamaddzikri0103.bookshelfnext.model.ReadingOld

@Database(entities = [Book::class, ReadingOld::class], version = 1, exportSchema = false)
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