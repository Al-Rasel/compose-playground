package com.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.composeplayground.navigation.AppNavigation
import com.composeplayground.navigation.rememberAppState
import com.composeplayground.ui.theme.ComposeplaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeplaygroundTheme {
                val appState = rememberAppState()
                AppNavigation(appState = appState)
            }
        }
    }
}