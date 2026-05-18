package com.miniluchi.jellyfin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.miniluchi.jellyfin.ui.navigation.AppNavGraph
import com.miniluchi.jellyfin.ui.theme.JellyfinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JellyfinTheme {
                AppNavGraph()
            }
        }
    }
}
