package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.muhamaddzikri0103.bookshelfnext.R
import com.muhamaddzikri0103.bookshelfnext.model.BookAndReading
import com.muhamaddzikri0103.bookshelfnext.navigation.Screen
import com.muhamaddzikri0103.bookshelfnext.ui.theme.BookShelfTheme
import com.muhamaddzikri0103.bookshelfnext.util.SettingsDataStore
//import com.muhamaddzikri0103.bookshelfnext.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                        dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.TrashScreen.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.bin),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.InsertForm.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_book),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        ScreenContent(showList, navController, Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(showList: Boolean, navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()

    val context = LocalContext.current
//    val factory = ViewModelFactory(context)
//    val viewModel: MainViewModel = viewModel(factory = factory)
//    val data by viewModel.data.collectAsState()
//    val data = emptyList<BookAndReading>()

//    if (data.isEmpty()) {
//        Column(
//            modifier = modifier.fillMaxSize().padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = stringResource(R.string.empty_list),
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//    else {
//        if (showList) {
//            LazyColumn(
//                modifier = modifier.fillMaxSize(),
//                contentPadding = PaddingValues(bottom = 84.dp)
//            ) {
//                items(data) {
//                    ListItem(bookNreading = it) {
//                        navController.navigate(Screen.DetailScreen.withId(it.readingId))
//                    }
//                    HorizontalDivider()
//                }
//            }
//        } else {
//            LazyVerticalStaggeredGrid(
//                modifier = modifier.fillMaxSize(),
//                columns = StaggeredGridCells.Fixed(2),
//                verticalItemSpacing = 8.dp,
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
//            ) {
//                items(data) {
//                    GridItem(bookNreading = it) {
//                        navController.navigate(Screen.DetailScreen.withId(it.readingId))
//                    }
//                }
//            }
//        }
//
//    }
}

@Composable
fun ListItem(bookNreading: BookAndReading, onClick: () -> Unit) {
    val numOfPages: Int = bookNreading.numOfPages
    val currentPage: Int = bookNreading.currentPage
    val pagesLeft: Int = numOfPages - currentPage
    val pct: Double = (currentPage.toDouble() / numOfPages.toDouble()) * 100
    val pctFormat = String.format(Locale.US, "%.0f", pct)

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = bookNreading.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = bookNreading.author,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = stringResource(R.string.x_left_x, pagesLeft.toString(), numOfPages.toString()))
        Text(
            text = stringResource(R.string.x_completed, pctFormat),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun GridItem(bookNreading: BookAndReading, onClick: () -> Unit) {
    val numOfPages: Int = bookNreading.numOfPages
    val currentPage: Int = bookNreading.currentPage
    val pagesLeft: Int = numOfPages - currentPage
    val pct: Double = (currentPage.toDouble() / numOfPages.toDouble()) * 100
    val pctFormat = String.format(Locale.US, "%.0f", pct)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, DividerDefaults.color)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = bookNreading.title,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = bookNreading.author,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = stringResource(R.string.x_left_x, pagesLeft.toString(), numOfPages.toString()))
            Text(
                text = stringResource(R.string.x_completed, pctFormat),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    BookShelfTheme {
        MainScreen(rememberNavController())
    }
}