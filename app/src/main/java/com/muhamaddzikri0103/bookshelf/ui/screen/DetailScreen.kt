package com.muhamaddzikri0103.bookshelf.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.muhamaddzikri0103.bookshelf.R
import com.muhamaddzikri0103.bookshelf.model.Book
import com.muhamaddzikri0103.bookshelf.model.BookAndReading
import com.muhamaddzikri0103.bookshelf.model.Reading
import com.muhamaddzikri0103.bookshelf.ui.theme.BookShelfTheme
import java.util.Locale

const val READING_KEY_ID = "readingId"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val viewModel: MainViewModel = viewModel()
    var data by remember { mutableStateOf<BookAndReading?>(null) }

    LaunchedEffect(Unit) {
        if (id == null) return@LaunchedEffect
        data = viewModel.getBookAndReading(id) ?: return@LaunchedEffect
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.reading_detail),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(R.string.other),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        data?.let { ReadingDetail(data = it, modifier = Modifier.padding(innerPadding)) }
    }
}

@Composable
fun ReadingDetail(data: BookAndReading, modifier: Modifier = Modifier) {
    val title = data.book.title
    val author = data.book.author
    val genre = data.book.genre
    val numOfPages = data.book.numOfPages
    var currentPage by remember { mutableStateOf(data.reading.currentPage) }

    val pagesLeft: Int = numOfPages - currentPage
    val pct: Double = (currentPage.toDouble() / numOfPages.toDouble()) * 100
    val pctFormat = String.format(Locale.US, "%.0f", pct)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Start
            )
            HorizontalDivider()
            Text(
                text = stringResource(R.string.author_x, author),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.genre_x, genre),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.total_pages_x, numOfPages.toString()),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.x_left, pagesLeft.toString()),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.x_completed, pctFormat),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ReadingDetailPreview() {
    val mockData = BookAndReading(
        id = 1,
        book = Book(
            id = 1,
            title = "Harry Potter and the Philosopher's Stone",
            author = "J.K. Rowling",
            genre = "Fantasy",
            numOfPages = 450
        ),
        reading = Reading(
            id = 1,
            currentPage = 30,
            isDeleted = false,
            bookId = 1
        )
    )

    BookShelfTheme {
        ReadingDetail(mockData)
    }
}
