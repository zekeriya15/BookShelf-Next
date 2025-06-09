package com.muhamaddzikri0103.bookshelfnext.navigation

import com.muhamaddzikri0103.bookshelfnext.ui.screen.READING_DETAIL_KEY_ID
import com.muhamaddzikri0103.bookshelfnext.ui.screen.USER_KEY_ID

sealed class Screen(val route: String) {
    data object Home: Screen("homeScreen")
    data object InsertForm: Screen("upsertScreen")
    data object DetailScreen: Screen("detailScreen/{$READING_DETAIL_KEY_ID}/{$USER_KEY_ID}") {
        fun withId(id: Int, userId: String) = "detailScreen/$id/$userId"
    }
    data object UpdateForm: Screen("upsertScreen/{$READING_DETAIL_KEY_ID}") {
        fun withId(id: Long) = "upsertScreen/$id"

    }
    data object TrashScreen: Screen("trashScreen/{$USER_KEY_ID}") {
        fun withId(userId: String) = "trashScreen/$userId"
    }
}