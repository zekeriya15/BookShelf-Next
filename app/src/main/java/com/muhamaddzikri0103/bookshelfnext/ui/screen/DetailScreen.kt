package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.muhamaddzikri0103.bookshelfnext.R
import com.muhamaddzikri0103.bookshelfnext.model.BookAndReading
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.muhamaddzikri0103.bookshelfnext.navigation.Screen
import com.muhamaddzikri0103.bookshelfnext.network.ApiStatus
//import com.muhamaddzikri0103.bookshelfnext.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val READING_DETAIL_KEY_ID = "readingDetailId"
const val USER_KEY_ID = "userId"

private val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
private val outputFormatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Int, userId: String) {
    val context: Context = LocalContext.current

    val viewModel: DetailViewModel = viewModel()
    val data by viewModel.currentReading.collectAsState()
    val status by viewModel.status.collectAsState()
    val errorMessage by viewModel.errorMessage

    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            bitmap = getCroppedImage(context.contentResolver, result)
            Log.d("DetailScreen", "Crop successful. Bitmap is null: ${bitmap == null}. Bitmap size: ${bitmap?.byteCount} bytes")
        } else {
            Log.e("DetailScreen", "Crop failed: ${result.error?.message}")
            bitmap = null // Ensure bitmap is null on failure
        }
    }

    var showUpsertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        viewModel.retrieveDataById(id, userId)
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
                    UpdateNDelete(
                        onEditClick = {
                            showUpsertDialog = true
                        },
                        onMoveClick = {
                            viewModel.softDelete(id, userId)
                            navController.popBackStack()
                            Toast.makeText(context, R.string.toast_move, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
        }
    ) { innerPadding ->

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }

        when (status) {

            ApiStatus.LOADING -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            ApiStatus.SUCCESS -> {
                if (data != null) {
                    ReadingDetail(
                        data = data!!,
                        userId = userId,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.empty_list),
                        textAlign = TextAlign.Center
                    )
                }
            }

            ApiStatus.FAILED -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.error),
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { viewModel.retrieveDataById(id, userId) },
                        modifier = Modifier.padding(top = 16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.try_again))
                    }
                }
            }
        }

        if (showUpsertDialog) {
            var deleteExistingImage by remember { mutableStateOf(false) }

            UpsertDialog(
                reading = data,
                bitmap = bitmap,
                onDeleteImage = {
                    bitmap = null
                    deleteExistingImage = true
                },
                onChangeImage = {
                    deleteExistingImage = false

                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true,
                            aspectRatioX = 2,
                            aspectRatioY = 3
                        )
                    )
                    launcher.launch(options)
                },
                onDismissRequest = {
                    showUpsertDialog = false
                    bitmap = null
                    deleteExistingImage = false
                },
                onAddConfirmation = null,
                onEditConfirmation = { title, author, genre, pages, currentPage, newBitmap ->
                    viewModel.updateData(
                        readingId = id,
                        userId = userId,
                        bitmap = newBitmap,
                        title = title,
                        author = author,
                        genre = genre,
                        pages = pages,
                        currentPage = currentPage,
                        deleteImage = deleteExistingImage
                    )
                    showUpsertDialog = false
                    bitmap = null
                    deleteExistingImage = false
                }
            )
        }
    }
}

@Composable
fun UpdateNDelete(
    onEditClick: () -> Unit,
    onMoveClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.other),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.edit_book))
                },
                onClick = {
                    expanded = false
                    onEditClick()
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.move_trash))
                },
                onClick = {
                    expanded = false
                    onMoveClick()
                }
            )
        }
    }
}

@Composable
fun ReadingDetail(data: Reading, userId: String, viewModel: DetailViewModel, modifier: Modifier = Modifier) {
    val imageUrl: String? = data.imageUrl
    val title = data.title
    val author = data.author
    val genre = data.genre
    val numOfPages = data.pages
    var dateModified by remember { mutableStateOf(data.dateModified) }
    val currentPage by remember { mutableIntStateOf(data.currentPage) }

    val pagesLeft: Int = numOfPages - currentPage
    val pct: Double = (currentPage.toDouble() / numOfPages.toDouble()) * 100
    val pctFormat = String.format(Locale.US, "%.0f", pct)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (imageUrl != null) {
            Column(
                modifier = modifier.fillMaxWidth().padding(bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)     // awalnya yg bener pake imageUrl
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.DISABLED) // Disable memory cache
                        .diskCachePolicy(CachePolicy.DISABLED)   // Disable disk cache
                        .build(),
                    contentDescription = stringResource(R.string.image, title),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.baseline_broken_image_24),
                    modifier = Modifier.size(180.dp)
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
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
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.x_left, pagesLeft.toString()),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.x_completed, pctFormat),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            val formattedDate = try {
                val parsedDate: Date? = inputFormatter.parse(dateModified)

                if (parsedDate != null) {
                    outputFormatter.format(parsedDate)
                } else {
                    Log.e("ReadingDetail", "Parsed date is null for: $dateModified")
                    dateModified
                }
            } catch (e: Exception) {
                Log.e("ReadingDetail", "Error parsing date: $dateModified", e)
                dateModified
            }
            Text(
                text = stringResource(R.string.last_updated_x, formattedDate),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Start
            )
            ButtonNCounter(data, userId, pagesLeft, viewModel)
        }
    }
}

@Composable
fun ButtonNCounter(
    data: Reading,
    userId: String,
    pagesLeft: Int,
    viewModel: DetailViewModel
) {
    val currentPage by remember { mutableIntStateOf(data.currentPage) }
    var isClicked by remember { mutableStateOf(false) }
    var amount by remember { mutableIntStateOf(0) }
    var totalPagesRead by remember { mutableIntStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { isClicked = !isClicked },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.update_progress))
        }

        if (isClicked) {
            val bottomPadding = if (data.imageUrl != null) 80 else 0

            Column(modifier = Modifier.padding(top = 16.dp, bottom = 5.dp)) {
                Text(text = stringResource(R.string.pages_read))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (amount != 0) {
                            amount--
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_remove_24),
                        contentDescription = stringResource(R.string.minus),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = amount.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = {
                        if (amount >= 0 && amount < pagesLeft) {
                            amount++
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = bottomPadding.dp)
            ) {
                IconButton(
                    onClick = {
                        isClicked = false
                        totalPagesRead = currentPage + amount
                        amount = 0

                        viewModel.addPages(data.id, userId, totalPagesRead)
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = stringResource(R.string.add_page),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ReadingDetailPreview() {
    DetailScreen(rememberNavController(), 1, "test@gmail.com")
}
