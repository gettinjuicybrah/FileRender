package com.joeybasile.filerender.QuickInsert

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun rememberDocState(): DocState {
    val scope = rememberCoroutineScope()

    val state = remember {
        DocState(
            scope = scope
        )
    }
    return state
}