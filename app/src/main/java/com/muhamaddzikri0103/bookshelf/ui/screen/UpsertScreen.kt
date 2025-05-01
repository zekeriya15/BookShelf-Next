package com.muhamaddzikri0103.bookshelf.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhamaddzikri0103.bookshelf.R
import com.muhamaddzikri0103.bookshelf.ui.theme.BookShelfTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertScreen() {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    
    val radioOptions = listOf(
        stringResource(R.string.romance),
        stringResource(R.string.mystery),
        stringResource(R.string.horror),
        stringResource(R.string.fantasy),
        stringResource(R.string.scifi),
        "",
        stringResource(R.string.biography),
        stringResource(R.string.self_help),
        stringResource(R.string.psychology),
        stringResource(R.string.history),
        stringResource(R.string.education),
    )

    var genre by remember { mutableStateOf(radioOptions[0]) }
    var pages by remember { mutableStateOf("") }
    var currPages by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.add_book))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        BookForm(
            title = title,
            onTitleChange = { title = it },
            author = author,
            onAuthorChange = { author = it },
            genre = genre,
            onGenreChange = { genre = it },
            pages = pages,
            onPagesChange = { pages = it },
            currPages = currPages,
            onCurrPagesChange = { currPages = it },
            radioOptions = radioOptions,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookForm(
    title: String, onTitleChange: (String) -> Unit,
    author: String, onAuthorChange: (String) -> Unit,
    genre: String, onGenreChange: (String) -> Unit,
    pages: String, onPagesChange: (String) -> Unit,
    currPages: String, onCurrPagesChange: (String) -> Unit,
    radioOptions: List<String>,
    modifier: Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

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
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = genre,
                onValueChange = {},
                label = { Text(text = stringResource(R.string.genre)) },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                radioOptions.forEach { value ->
                    if (value == "") {
                        HorizontalDivider()
                    } else {
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            text = { Text(text = value) },
                            onClick = {
                                onGenreChange(value)
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            value = pages,
            onValueChange = { onPagesChange(it) },
            label = { Text(text = stringResource(R.string.page_num)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = currPages,
            onValueChange = { onCurrPagesChange(it) },
            label = { Text(text = stringResource(R.string.curr_page)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun UpsertScreenPreview() {
    BookShelfTheme {
        UpsertScreen()
    }
}