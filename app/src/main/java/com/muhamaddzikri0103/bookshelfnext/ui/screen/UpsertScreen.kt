package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.muhamaddzikri0103.bookshelfnext.R
import com.muhamaddzikri0103.bookshelfnext.ui.theme.BookShelfTheme
//import com.muhamaddzikri0103.bookshelfnext.util.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
//    val factory = ViewModelFactory(context)
//    val viewModel: UpsertViewModel = viewModel(factory = factory)
//
//    var bookId by remember { mutableLongStateOf(0) }
//    var title by remember { mutableStateOf("") }
//    var author by remember { mutableStateOf("") }
//
//    val radioOptions = listOf(
//        stringResource(R.string.romance),
//        stringResource(R.string.mystery),
//        stringResource(R.string.horror),
//        stringResource(R.string.fantasy),
//        stringResource(R.string.scifi),
//        "",
//        stringResource(R.string.biography),
//        stringResource(R.string.self_help),
//        stringResource(R.string.psychology),
//        stringResource(R.string.history),
//        stringResource(R.string.education),
//    )
//
//    var genre by remember { mutableStateOf(radioOptions[0]) }
//    var pages by remember { mutableStateOf("") }
//    var currPages by remember { mutableStateOf("") }
//    var dateModified by remember { mutableStateOf("") }
//
//    LaunchedEffect(id) {
//        if (id == null) return@LaunchedEffect
//        viewModel.getBookAndReadingById(id).collectLatest { bookAndReading ->
//            bookId = bookAndReading.bookId
//            title = bookAndReading.title
//            author = bookAndReading.author
//            genre = bookAndReading.genre
//            pages = bookAndReading.numOfPages.toString()
//            currPages = bookAndReading.currentPage.toString()
//            dateModified = bookAndReading.dateModified
//        }
//    }
////
////    Scaffold(
////        topBar = {
////            TopAppBar(
////                navigationIcon = {
////                    IconButton(onClick = { navController.popBackStack() }) {
////                        Icon(
////                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
////                            contentDescription = stringResource(R.string.back),
////                            tint = MaterialTheme.colorScheme.primary
////                        )
////                    }
////                },
////                title = {
////                    if (id == null)
////                        Text(text = stringResource(id = R.string.add_book))
////                    else
////                        Text(text = stringResource(id = R.string.edit_book))
////                },
////                colors = TopAppBarDefaults.mediumTopAppBarColors(
////                    containerColor = MaterialTheme.colorScheme.primaryContainer,
////                    titleContentColor = MaterialTheme.colorScheme.primary
////                ),
////                actions = {
////                    IconButton(onClick = {
////                        val curr = currPages.toIntOrNull() ?: 0
////                        val total = pages.toIntOrNull() ?: 0
////
////                        if ((title == "" || author == "" || genre == "" || pages == "" || pages == "0")
////                            && id == null
////                        ) {
////                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_SHORT).show()
////                            return@IconButton
////                        }
////                        if ((title == "" || author == "" || genre == "" || pages == "" || pages == "0")
////                            && id != null
////                        ) {
////                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_SHORT).show()
////                            return@IconButton
////                        }
////                        if ((curr > total) && id != null) {
////                            Toast.makeText(context, R.string.invalid_pages, Toast.LENGTH_SHORT).show()
////                            return@IconButton
////                        }
////                        if (id == null) {
////                            viewModel.insert(title, author, genre, pages)
////                        } else {
////                            viewModel.update(
////                                bookId = bookId,
////                                title = title,
////                                author = author,
////                                genre = genre,
////                                numOfPages = pages,
////                                readingId = id,
////                                currentPage = currPages
////                            )
////                        }
////                        navController.popBackStack()
////                    }) {
////                        Icon(
////                            imageVector = Icons.Outlined.Check,
////                            contentDescription = stringResource(R.string.save),
////                            tint = MaterialTheme.colorScheme.primary
////                        )
////                    }
////                }
////            )
////        }
//    ) { padding ->
//        BookForm(
//            id = id,
//            title = title,
//            onTitleChange = { title = it },
//            author = author,
//            onAuthorChange = { author = it },
//            genre = genre,
//            onGenreChange = { genre = it },
//            pages = pages,
//            onPagesChange = { pages = it },
//            currPages = currPages,
//            onCurrPagesChange = { currPages = it },
//            radioOptions = radioOptions,
//            modifier = Modifier.padding(padding)
//        )
//    }
}

@Composable
fun BookForm(
    id: Long? = null,
    title: String, onTitleChange: (String) -> Unit,
    author: String, onAuthorChange: (String) -> Unit,
    genre: String, onGenreChange: (String) -> Unit,
    pages: String, onPagesChange: (String) -> Unit,
    currPages: String, onCurrPagesChange: (String) -> Unit,
    radioOptions: List<String>,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.title)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = author,
            onValueChange = { onAuthorChange(it) },
            label = { Text(text = stringResource(R.string.author)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        GenreDropdown(
            genre = genre,
            onGenreChange = { onGenreChange(it) },
            radioOptions = radioOptions
        )

        OutlinedTextField(
            value = pages,
            onValueChange = { if (it.isDigitsOnly()) onPagesChange(it) },
            label = { Text(text = stringResource(R.string.page_num)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = if (id == null) ImeAction.Done else ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (id != null) {
            HorizontalDivider()
            OutlinedTextField(
                value = currPages,
                onValueChange = { if (it.isDigitsOnly()) onCurrPagesChange(it) },
                label = { Text(text = stringResource(R.string.curr_page)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreDropdown(
    genre: String,
    onGenreChange: (String) -> Unit,
    radioOptions: List<String>
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = genre,
            onValueChange = {},
            readOnly = true,
            label = { Text("Genre") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            radioOptions.forEach { value ->
                if (value == "") {
                    HorizontalDivider()
                } else {
                    DropdownMenuItem(
                        text = { Text(value) },
                        onClick = {
                            onGenreChange(value)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun UpsertScreenPreview() {
    BookShelfTheme {
        UpsertScreen(rememberNavController())
    }
}