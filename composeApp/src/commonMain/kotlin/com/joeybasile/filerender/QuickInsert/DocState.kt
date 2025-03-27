package com.joeybasile.filerender.QuickInsert

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent


data class SpanStyleRange(val range: TextRange, val style: SpanStyle)

class DocState(val scope: CoroutineScope,){

    val cursorCaretState = DocCursorCaretState(this)

    val focusedLine = mutableStateOf(0)


    val fieldListState = mutableStateListOf<FieldEntry>(FieldEntry(
        FieldType.TextType(
            BasicTextFieldType(
                AnnotatedString("basic entry"),
                "test",
                focusRequester = FocusRequester()
            )
        )
    ),
        FieldEntry(
            FieldType.TextType(
                BasicTextFieldType(
                    AnnotatedString("basic entry"),
                    "test",
                    focusRequester = FocusRequester()
                )
            )
        ),
        FieldEntry(
            FieldType.TextType(
                BasicTextFieldType(
                    AnnotatedString("basic entry"),
                    "test",
                    focusRequester = FocusRequester()
                )
            )
        ))

    val fieldValListState = mutableStateListOf<MutableMap<BasicTextFieldType, TextFieldValue>>().apply {
        fieldListState.forEachIndexed { index, fieldEntry ->
            if (fieldEntry.type is FieldType.TextType)
                this.add(
                    mutableStateMapOf(
                        fieldEntry.type.textField to TextFieldValue(
                            fieldEntry.type.textField.content.text,
                            TextRange(fieldEntry.type.textField.content.text.length)
                        )
                    )
                )
        }
    }

/*
Calculates the position of the caret in the document.
Returns a CursorCaretData object containing the position and height of the caret.
Determined via the current focused line.
 */
    fun calculateCursorCaretPosition(): CursorCaretData {

    }

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