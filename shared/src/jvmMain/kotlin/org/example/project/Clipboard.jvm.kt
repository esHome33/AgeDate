package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
actual fun rememberCopyTextToClipboard(): (String) -> Boolean {
    return remember {
        { text ->
            runCatching {
                val selection = StringSelection(text)
                Toolkit.getDefaultToolkit()
                    .systemClipboard
                    .setContents(selection, null)
            }.isSuccess
        }
    }
}