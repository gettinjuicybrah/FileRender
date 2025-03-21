package com.joeybasile.filerender.QuickInsert.State

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size

//data class CursorData()

data class CursorState(
    val pos: Int = 0,
    val selected: Boolean = false
)
//need scroll manager
//data class ScrollState()

class QuickInsertTextEditorState {

    val cursorState = TextEditorCursorState(this)
    //val cursorPos = cursor.pos
    internal var viewportSize by mutableStateOf(Size(1f, 1f))
    var isFocused by mutableStateOf(false)


    fun updateFocus(focused: Boolean) {
        isFocused = focused
    }

    fun onViewportSizeChange(size: Size) {
        viewportSize = size

    }

}