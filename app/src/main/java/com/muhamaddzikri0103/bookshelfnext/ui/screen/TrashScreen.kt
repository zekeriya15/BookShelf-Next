package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.muhamaddzikri0103.bookshelfnext.R
import com.muhamaddzikri0103.bookshelfnext.model.Reading
import com.muhamaddzikri0103.bookshelfnext.network.ApiStatus
import com.muhamaddzikri0103.bookshelfnext.ui.theme.BookShelfTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(navController: NavHostController, userId: String) {
    val viewModel: TrashViewModel = viewModel()

    val deletedDatas by viewModel.deletedDatas
    val status by viewModel.status.collectAsState()
    val errorMessage by viewModel.errorMessage

    LaunchedEffect(userId) {
        viewModel.retrieveDeletedDatas(userId)
    }

    val context = LocalContext.current

    var showDeleteAllDialog by remember { mutableStateOf(false) }

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
                    Text(text = stringResource(id = R.string.bin))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    DeleteAllMenu(onDelete = { showDeleteAllDialog = true })
                }
            )
        }
    ) { innerPadding ->

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }

        when (status) {
            ApiStatus.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            ApiStatus.SUCCESS -> {
                TrashContent(deletedDatas, userId, viewModel, Modifier.padding(innerPadding))
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
                        onClick = { viewModel.retrieveDeletedDatas(userId) },
                        modifier = Modifier.padding(top = 16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.try_again))
                    }
                }
            }
        }

        if (showDeleteAllDialog) {
            DisplayAlertDialog(
                displayText = stringResource(R.string.confirm_delete_all),
                confirmText = stringResource(R.string.delete),
                dismissText = stringResource(R.string.cancel),
                onDismissRequest = { showDeleteAllDialog = false },
                onConfirmation = {
                    viewModel.deleteAllTrash(userId)
                    Toast.makeText(context, R.string.toast_deleted_all, Toast.LENGTH_SHORT).show()
                    showDeleteAllDialog = false
                }
            )
        }
    }
}

@Composable
fun TrashContent(data: List<Reading>, userId: String, viewModel: TrashViewModel, modifier: Modifier = Modifier) {
    val context: Context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<Reading?>(null) }

    if (data.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.nodata_bin),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 84.dp)
        ) {
            items(data) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        ListItem(reading = item) {}
                    }
                    TrashItemMenu(
                        onRestore = {
                            viewModel.restoreData(item.id, userId)
                            Toast.makeText(context, R.string.toast_restore, Toast.LENGTH_SHORT).show()
                        },
                        onDelete = {
                            itemToDelete = item
                            showDialog = true
                        }
                    )
                }
                HorizontalDivider()
            }
        }
    }

    if (showDialog && itemToDelete != null) {
        DisplayAlertDialog(
            displayText = stringResource(R.string.confirm_delete),
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel),
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                viewModel.hardDelete(itemToDelete!!.id, userId)
                Toast.makeText(context, R.string.toast_deleted, Toast.LENGTH_SHORT).show()
                showDialog = false
            }
        )
    }
}

@Composable
fun TrashItemMenu(
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.other),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.restore)) },
                onClick = {
                    expanded = false
                    onRestore()
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.delete_permanently)) },
                onClick = {
                    expanded = false
                    onDelete()
                }
            )
        }
    }
}

@Composable
fun DeleteAllMenu(onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.other),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.delete_all)) },
                onClick = {
                    expanded = false
                    onDelete()
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun TrashScreenPreview() {
    BookShelfTheme {
        TrashScreen(rememberNavController(), "test")
    }
}