package com.joeybasile.filerender.QuickInsert.State

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TextEditorCursorState(
    private val editorState: QuickInsertTextEditorState
) {

    private var _isVisible by mutableStateOf(true)
    val isVisible: Boolean get() = _isVisible

    fun setVisibleTrue() {
        _isVisible = true
    }
    fun toggleVisibility() {
        _isVisible = !_isVisible
    }
}
