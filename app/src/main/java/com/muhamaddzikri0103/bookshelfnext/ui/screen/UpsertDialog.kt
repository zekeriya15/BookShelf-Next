package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.muhamaddzikri0103.bookshelfnext.R
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.muhamaddzikri0103.bookshelfnext.ui.theme.BookShelfTheme

@Composable
fun UpsertDialog(
    reading: Reading?,
    bitmap: Bitmap?,
    onDeleteImage: () -> Unit,
    onChangeImage: () -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: (title: String,
                     author: String,
                     genre: String,
                     numOfPages: Int,
                     bitmap: Bitmap?) -> Unit
) {
    val context: Context = LocalContext.current

//    var bookId by remember { mutableLongStateOf(0) }
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
//    var currPages by remember { mutableStateOf("") }
//    var dateModified by remember { mutableStateOf("") }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = if (bitmap != null) {
                        remember(bitmap) { BitmapPainter(bitmap.asImageBitmap()) }
                    } else {
                        painterResource(R.drawable.baseline_broken_image_24)
                    },
                    contentDescription = null,
                    modifier = Modifier.width(90.dp)
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(10.dp))
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            onDeleteImage()
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.delete))
                    }
                    OutlinedButton(
                        onClick = { onChangeImage() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        if (bitmap != null)
                            Text(text = stringResource(R.string.change))
                        else
                            Text(text = stringResource(R.string.add))

                    }
                }

                BookForm(
//                    isEditing = reading != null,
                    isEditing = false,
                    title = title,
                    onTitleChange = { title = it },
                    author = author,
                    onAuthorChange = { author = it },
                    genre = genre,
                    onGenreChange = { genre = it },
                    pages = pages,
                    onPagesChange = { pages = it },
                    currPages = "",
                    onCurrPagesChange = { },
                    radioOptions = radioOptions,
                    modifier = Modifier.fillMaxWidth() // Ensure form fills width within card
                )

                val isFormInvalid = title.isBlank() || author.isBlank() ||
                        genre.isBlank() || pages.isBlank() || pages == "0"

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    OutlinedButton(
                        onClick = {
                            val totalPagesInt = pages.toInt()

                            if (isFormInvalid) {
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_SHORT).show()
                                return@OutlinedButton
                            }

                            onConfirmation(title, author, genre, totalPagesInt, bitmap)
                            onDismissRequest()
                        },
                        enabled = !isFormInvalid,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

@Composable
fun BookForm(
    isEditing: Boolean,
    title: String, onTitleChange: (String) -> Unit,
    author: String, onAuthorChange: (String) -> Unit,
    genre: String, onGenreChange: (String) -> Unit,
    pages: String, onPagesChange: (String) -> Unit,
    currPages: String, onCurrPagesChange: (String) -> Unit,
    radioOptions: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
                imeAction = if (isEditing) ImeAction.Next else ImeAction.Done // Adjust imeAction based on editing
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (isEditing) { // Show currPages only if in editing mode
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
fun UpsertDialogPreview() {
    BookShelfTheme {
        UpsertDialog(
            reading = null,
            bitmap = null,
            onDeleteImage = { },
            onChangeImage = { },
            onDismissRequest = { },
            onConfirmation = { _, _, _, _, _-> }
        )
    }
}