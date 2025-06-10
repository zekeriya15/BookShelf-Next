package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.muhamaddzikri0103.bookshelfnext.BuildConfig
import com.muhamaddzikri0103.bookshelfnext.R
import com.muhamaddzikri0103.bookshelfnext.model.BookAndReading
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.muhamaddzikri0103.bookshelfnext.model.User
import com.muhamaddzikri0103.bookshelfnext.navigation.Screen
import com.muhamaddzikri0103.bookshelfnext.network.ApiStatus
import com.muhamaddzikri0103.bookshelfnext.ui.theme.BookShelfTheme
import com.muhamaddzikri0103.bookshelfnext.network.SettingsDataStore
//import com.muhamaddzikri0103.bookshelfnext.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current

    val dataStore = SettingsDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())
    val showList by dataStore.layoutFlow.collectAsState(true)

    val viewModel: MainViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

    var showDialog by remember { mutableStateOf(false) }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
    }

    var showUpsertDialog by remember { mutableStateOf(false) }

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
                    if (user.email.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.TrashScreen.withId(user.email))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.bin),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        }
                        else {
                            showDialog = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_account_circle_24),
                            contentDescription = stringResource(R.string.profile),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (user.email.isNotEmpty()) {
                        showUpsertDialog = true
                        bitmap = null
                    }
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
        ScreenContent(showList, user.email, viewModel, navController, Modifier.padding(innerPadding))

        if (showDialog) {
            ProfilDialog(
                user = user,
                onDismissRequest = { showDialog = false },
                onConfirmation = {
                    CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                    showDialog = false
                }
            )
        }

        if (showUpsertDialog) {
            UpsertDialog(
                reading = null,
                bitmap = bitmap,
                onDeleteImage = { bitmap = null },
                onChangeImage = {
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
                onDismissRequest = { showUpsertDialog = false },
                onConfirmation = { title, author, genre, pages, bitmap ->
//                    Log.d("TAMBAH", "$title, $author, $genre, $numOfPages")
                    viewModel.saveData(
                        userId = user.email,
                        bitmap = bitmap,
                        title = title,
                        author = author,
                        genre = genre,
                        pages = pages
                        )
                    showUpsertDialog = false
                }
            )
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }
}

@Composable
fun ScreenContent(showList: Boolean,
                  userId: String,
                  viewModel: MainViewModel,
                  navController: NavHostController,
                  modifier: Modifier = Modifier
) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    LaunchedEffect(userId) {
        viewModel.retrieveData(userId)
    }

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCESS -> {
            if (data.isEmpty() && userId.isNotEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.empty_list),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else if (userId.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.not_loggedin),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else {
                if (showList) {
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 84.dp)
                    ) {
                        items(data) {
                            ListItem(reading = it) {
                                navController.navigate(Screen.DetailScreen.withId(it.id, userId))
                            }
                            HorizontalDivider()
                        }
                    }
                }
//                else {
//                    LazyVerticalStaggeredGrid(
//                        modifier = modifier.fillMaxSize(),
//                        columns = StaggeredGridCells.Fixed(2),
//                        verticalItemSpacing = 8.dp,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                        contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
//                    ) {
//                        items(data) {
//                            GridItem(bookNreading = it) {
//                                navController.navigate(Screen.DetailScreen.withId(it.readingId))
//                            }
//                        }
//                    }
//                }
            }
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.error),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { viewModel.retrieveData(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.try_again),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ListItem(reading: Reading, onClick: () -> Unit) {
    val imageUrl: String? = reading.imageUrl
    val numOfPages: Int = reading.pages
    val currentPage: Int = reading.currentPage
    val pagesLeft: Int = numOfPages - currentPage
    val pct: Double = (currentPage.toDouble() / numOfPages.toDouble()) * 100
    val pctFormat = String.format(Locale.US, "%.0f", pct)

//    cek device bisa akses http dari ngrok or not
    LaunchedEffect(imageUrl) {
        if (imageUrl != null) {
            withContext(Dispatchers.IO) {
                try {
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url(imageUrl)
                        .build()

                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            Log.d("ImageTest", "Image loaded successfully: ${response.code}")
                        } else {
                            Log.e("ImageTest", "Image load failed: ${response.code} - ${response.message}")
                            // Log the response body if it's an error
                            Log.e("ImageTest", "Response Body: ${response.body?.string()}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ImageTest", "Image network error: ${e.message}", e)
                }
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var columnPadding = 0

        if (imageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.image, reading.title),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.baseline_broken_image_24),
                modifier = Modifier.width(80.dp)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(10.dp))
            )

            columnPadding = 16
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(start = columnPadding.dp),
//                .clickable { onClick() },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = reading.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = reading.author,
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

private suspend fun signIn(context: Context, dataStore: SettingsDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: SettingsDataStore
) {
    val credential = result.credential
    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
//            Log.d("SIGN-IN", "User email: ${googleId.id}")
            val name = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(name, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error ${e.message}")
        }
    }
    else {
        Log.e("SIGN-IN", "Error: unrecognized customcredential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: SettingsDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error ${e.errorMessage}")
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
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