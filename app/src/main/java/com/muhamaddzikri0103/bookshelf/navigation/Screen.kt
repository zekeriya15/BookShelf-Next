package com.muhamaddzikri0103.bookshelf.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("homeScreen")
    data object InsertForm: Screen("upsertScreen")
}