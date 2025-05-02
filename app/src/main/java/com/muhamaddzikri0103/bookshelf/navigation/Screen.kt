package com.muhamaddzikri0103.bookshelf.navigation

import com.muhamaddzikri0103.bookshelf.ui.screen.READING_KEY_ID

sealed class Screen(val route: String) {
    data object Home: Screen("homeScreen")
    data object InsertForm: Screen("upsertScreen")
    data object DetailScreen: Screen("detailScreen/{$READING_KEY_ID}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}