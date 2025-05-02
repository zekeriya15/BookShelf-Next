package com.muhamaddzikri0103.bookshelf.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.muhamaddzikri0103.bookshelf.ui.screen.MainScreen
import com.muhamaddzikri0103.bookshelf.ui.screen.UpsertScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
   NavHost(
       navController = navController,
       startDestination = Screen.Home.route
   ) {
       composable(route = Screen.Home.route) {
           MainScreen(navController)
       }
       composable(route = Screen.InsertForm.route) {
           UpsertScreen()
       }
   }
}