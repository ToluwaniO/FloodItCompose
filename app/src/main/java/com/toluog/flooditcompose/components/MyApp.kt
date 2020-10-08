package com.toluog.flooditcompose.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.toluog.flooditcompose.ui.FloodItComposeTheme

@Composable
fun MyApp(component: @Composable () -> Unit) {
    FloodItComposeTheme {
        Surface(color = Color.White) {
            component()

        }

    }
}
