package com.joeybasile.filerender.QuickInsert

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyDown
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyUp
import androidx.compose.ui.input.key.KeyEventType.Companion.Unknown
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.koinInject

data class BasicTextFieldType(
    val content: AnnotatedString,
    val label: String,
    val maxLength: Int = 100,
    val isRequired: Boolean = false,
    val placeholder: String = "",
    val focusRequester: FocusRequester
)

data class BasicImageFieldType(val image: String?)

sealed class FieldType {
    data class TextType(val textField: BasicTextFieldType) : FieldType()
    data class ImageType(val imageField: BasicImageFieldType) : FieldType()
}

data class FieldEntry(

    val type: FieldType? = null
)

@Composable
fun WYSIWYGEditor() {
    var text by remember { mutableStateOf("Type here...") }

    DynamicWidthBasicTextField(
        value = text,
        onValueChange = { text = it },
        minWidth = 100.dp,
        maxWidth = 300.dp
    )
}

@Composable
fun DynamicWidthBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    minWidth: Dp = 100.dp,
    maxWidth: Dp = 300.dp,
    modifier: Modifier = Modifier
) {
    Layout(
        content = {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray) // Adds a visible gray border
                    ) {
                        innerTextField() // Places the text field inside the box
                    }
                }
            )
        },
        modifier = modifier
    ) { measurables, constraints ->
        // Measure the BasicTextField with unconstrained width to get its intrinsic width
        val placeable = measurables[0].measure(
            constraints.copy(minWidth = 0, maxWidth = Int.MAX_VALUE)
        )

        // Clamp the width between minWidth and maxWidth (converted to pixels)
        val width = placeable.width.coerceIn(
            minWidth.roundToPx(),
            maxWidth.roundToPx()
        )

        // Define the layout size and place the text field
        layout(width, placeable.height) {
            placeable.placeRelative(x = 0, y = 0)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicTextEntry(
    textEntry: BasicTextFieldType,
    index: Int,
    focusedLine: MutableState<Int>,
    focusManager: FocusManager,
    fieldState: SnapshotStateList<FieldEntry>,
    fieldValList: SnapshotStateList<MutableMap<BasicTextFieldType, TextFieldValue>>
) {



    var value by remember {
        mutableStateOf(
            TextFieldValue(
                textEntry.content,
                TextRange(textEntry.content.length)
            )
        )
    }


    var isLaidOut by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    //var textValState = fieldValList[index][textEntry]!!
        //println("after entry")
        println()
        BasicTextField(
            value = fieldValList[index][textEntry]!!,
            onValueChange = { newValue ->
                fieldValList[index][textEntry] = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                //.padding(vertical = 4.dp)
                .focusRequester(textEntry.focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        isFocused = true
                        println("focus updated to index $index")
                        focusedLine.value = index
                    } else{
                        isFocused = false
                    }
                }
                .background(
                    color = if (isFocused) Color.LightGray else Color.Transparent,
            shape = RoundedCornerShape(4.dp)
        )
                // Intercept key events to move focus up/down
                .onPreviewKeyEvent {
                    when (it.type) {
                        KeyUp -> when (it.key) {
                            Key.DirectionDown -> {
                                if (index < fieldState.size - 1) {
                                    println("down arrow pressed")
                                    //focusManager.moveFocus(FocusDirection.Down)
                                    focusedLine.value = index+1
                                }
                                true
                            }

                            Key.DirectionUp -> {
                                if (index > 0) {
                                    println("up arrow pressed")
                                    //focusManager.moveFocus(FocusDirection.Up)
                                    focusedLine.value = index-1
                                }
                                true
                            }

                            Key.Enter -> {
                                for (entry in fieldState) {
                                    println("ENTRIES BEFORE ADD: $entry")
                                    println("ENTRY INDEX: $index")
                                }
                                println("*******************************************************************enter pressed")
                                val newEntry = FieldEntry(
                                    FieldType.TextType(
                                        BasicTextFieldType(
                                            AnnotatedString("new entry folk"), "test",
                                            focusRequester = FocusRequester()
                                        )
                                    )
                                )
                                println("FIELDSTATE BEFORE ADDING: $fieldState")
                                // Insert it after the current one
                                fieldState.add(index + 1, newEntry)

                                // Add the new entry to fieldValMap

                                if(newEntry.type is FieldType.TextType)
                                fieldValList.add(index + 1, mutableStateMapOf(newEntry.type.textField to TextFieldValue(newEntry.type.textField.content, TextRange(newEntry.type.textField.content.text.length))))


                                println("FIELDSTATE AFTER ADDING: $fieldState")

                                focusedLine.value = index + 1
                                for (entry in fieldState) {
                                    println("NEW ENTTRIES AFTER ADD: $entry")
                                    println("ENTRY INDEX: $index")
                                }


                                true
                            }

                            else -> {
                                println("other key pressed")
                                println(it.key)
                            }
                        }

                        KeyDown -> when(it.key){
                            Key.Enter -> {
                                println("enter key down")
                            }
                        }

                        Unknown -> println("Unknown key type")
                        else -> println("New KeyType (for future use)")
                    }
                    false
                },
            //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true
        )
   // }
    LaunchedEffect(focusedLine.value) {
        if (focusedLine.value == index && index < fieldState.size) {

            try {
                //println("FOCUS REQUESTED IN BASICTEXTENTRY$index")
                textEntry.focusRequester.requestFocus()
            } catch (e: Exception) {
                // Handle exception gracefully
            }
        }
    }

}

@Composable
fun BasicEditor() {
    val focusedLine = remember { mutableStateOf(0) }

    val fieldState = remember {
        mutableStateListOf(
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
            ),
            FieldEntry(
                FieldType.TextType(
                    BasicTextFieldType(
                        AnnotatedString("basic entry"),
                        "test",
                        focusRequester = FocusRequester()
                    )
                )
            )
            )

    }
    val fieldValList = remember {
        mutableStateListOf<MutableMap<BasicTextFieldType, TextFieldValue>>().apply {
            fieldState.forEachIndexed { index, fieldEntry ->
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

    }

    val focusRequesters = remember { mutableStateListOf<FocusRequester>() }
    val focusManager = LocalFocusManager.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        println("lazy test. current focused line: $focusedLine")
        itemsIndexed(fieldState) { index, fieldEntry ->
/*
            if (index >= focusRequesters.size) {
                //println("added new")
                focusRequesters.add(FocusRequester())
            }

 */
            key(index) {
                //println("KEY INDEX TEST")
                if (fieldEntry.type is FieldType.TextType) {
                    println("TEXT FIELD INDEX: $index")
                    println("TEXT FIELD ENTRY: ${fieldEntry.type.textField.content}")
                    BasicTextEntry(
                        fieldEntry.type.textField,
                        index,
                        focusedLine,
                        focusManager,
                        fieldState,
                        fieldValList
                    )
                }
            }
        }
    }
}

@Composable
fun RichTextEditor() {
// State to hold our text lines
    val lines = remember { mutableStateListOf("") }

    // Keep track of the currently focused line
    val focusedLine = remember { mutableStateOf(0) }

    // Create a list of focus requesters, one for each line
    val focusRequesters = remember { mutableStateListOf(FocusRequester()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        itemsIndexed(lines) { index, text ->
            // Ensure we have enough focus requesters
            if (index >= focusRequesters.size) {
                focusRequesters.add(FocusRequester())
            }

            val textFieldValue = remember(text) {
                mutableStateOf(TextFieldValue(text, TextRange(text.length)))
            }

            // Create a key for each text field to ensure proper recomposition
            key(index) {
                BasicTextField(
                    value = textFieldValue.value,
                    onValueChange = { newValue ->
                        // Check if Enter was pressed (new line character detected)
                        if (newValue.text.contains('\n')) {
                            // Split the text at the cursor position
                            val cursorPosition = newValue.selection.start
                            val currentText = newValue.text.replace("\n", "")
                            val beforeCursor = if (cursorPosition > 0) currentText.substring(
                                0,
                                cursorPosition - 1
                            ) else ""
                            val afterCursor =
                                if (cursorPosition - 1 < currentText.length) currentText.substring(
                                    cursorPosition - 1
                                ) else ""

                            // Update current line
                            lines[index] = beforeCursor

                            // Insert new line
                            lines.add(index + 1, afterCursor)

                            // Add a new focus requester
                            if (index + 1 >= focusRequesters.size) {
                                focusRequesters.add(FocusRequester())
                            }

                            // Update the focused line
                            focusedLine.value = index + 1
                        } else {
                            // Normal text update
                            lines[index] = newValue.text
                            textFieldValue.value = newValue
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .focusRequester(focusRequesters[index])
                        .onFocusChanged {
                            if (it.isFocused) {
                                focusedLine.value = index
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = if (focusedLine.value == index) Color.Blue else Color.LightGray,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp)
                        ) {
                            innerTextField()
                        }
                    }
                )
            }

            // Focus the newly created line using LaunchedEffect - this is crucial for fixing the focus issue
            LaunchedEffect(focusedLine.value) {
                if (focusedLine.value == index && index < focusRequesters.size) {
                    try {
                        delay(500) // Small delay to ensure the composition is complete
                        focusRequesters[index].requestFocus()
                    } catch (e: Exception) {
                        // Handle exception gracefully
                    }
                }
            }
        }
    }

    // Initial focus for the first line
    LaunchedEffect(Unit) {
        delay(300) // Allow composition to complete
        if (focusRequesters.isNotEmpty()) {
            focusRequesters[0].requestFocus()
        }
    }
}