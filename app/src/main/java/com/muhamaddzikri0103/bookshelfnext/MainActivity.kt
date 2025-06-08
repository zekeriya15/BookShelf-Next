package com.muhamaddzikri0103.bookshelfnext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.muhamaddzikri0103.bookshelfnext.navigation.SetupNavGraph
import com.muhamaddzikri0103.bookshelfnext.ui.theme.BookShelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookShelfTheme {
                SetupNavGraph()
            }
        }
    }
}

