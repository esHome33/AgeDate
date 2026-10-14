package org.example.project

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(width = 650.dp, height = 850.dp)
    Window(
        onCloseRequest = ::exitApplication,
        title = "AgeDate",
        state = state
    ) {
        App()
    }
}