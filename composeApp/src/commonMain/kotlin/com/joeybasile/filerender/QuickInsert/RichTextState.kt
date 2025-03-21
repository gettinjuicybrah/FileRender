package com.joeybasile.filerender.QuickInsert

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.forEachChange
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.Koin
import org.koin.core.component.KoinComponent


data class SpanStyleRange(val range: TextRange, val style: SpanStyle)

class RichTextState(){

    //private val _fieldListState = MutableStateFlow(FieldEntryList())
    //val fieldListState: StateFlow<FieldEntryList> get() = _fieldListState.asStateFlow()

}
/*
data class BasicTextFieldType(
    val content: AnnotatedString,
    val label: String,
    val maxLength: Int = 100,
    val isRequired: Boolean = false,
    val placeholder: String = ""
)
data class BasicImageFieldType(val image: String?)

sealed class FieldType {
    data class TextType(val textField: BasicTextFieldType) : FieldType()
    data class ImageType(val imageField: BasicImageFieldType) : FieldType()
}

data class FieldEntry(
    val index: Int? = null,
    val type: FieldType? = null
)
data class FieldEntryList(
    val fields: List<FieldEntry>? = emptyList()
)

 */