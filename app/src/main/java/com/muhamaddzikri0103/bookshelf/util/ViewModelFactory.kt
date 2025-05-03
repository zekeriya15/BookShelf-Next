package com.muhamaddzikri0103.bookshelf.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muhamaddzikri0103.bookshelf.database.BookshelfDb
import com.muhamaddzikri0103.bookshelf.ui.screen.DetailViewModel
import com.muhamaddzikri0103.bookshelf.ui.screen.MainViewModel
import com.muhamaddzikri0103.bookshelf.ui.screen.UpsertViewModel

class ViewModelFactory (
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = BookshelfDb.getInstance(context).dao
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(UpsertViewModel::class.java)) {
            return UpsertViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}