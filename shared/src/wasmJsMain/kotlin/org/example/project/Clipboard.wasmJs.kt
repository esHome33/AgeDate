package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.window

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun rememberCopyTextToClipboard(): (String) -> Boolean {
    return remember {
        { text ->
            runCatching {
                window.navigator.clipboard.writeText(text)
                true
            }.getOrElse { false }
        }
    }
}
