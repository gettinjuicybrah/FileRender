package com.joeybasile.filerender.QuickInsert

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyDown
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyUp
import androidx.compose.ui.input.key.KeyEventType.Companion.Unknown
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.foundation.lazy.LazyColumn

@Composable
fun DocEditor(
    state: DocState = rememberDocState()
){


    val focusedLine = state.focusedLine
    val fieldState = state.fieldListState
    val fieldValList = state.fieldValListState

    Box(){
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


}

@Composable
fun TextEntry(
    textEntry: BasicTextFieldType,
    index: Int,
    focusedLine: MutableState<Int>,
    focusManager: FocusManager,
    fieldState: SnapshotStateList<FieldEntry>,
    fieldValList: SnapshotStateList<MutableMap<BasicTextFieldType, TextFieldValue>>
) {
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        value = fieldValList[index][textEntry]!!,
        onValueChange = { newValue ->
            fieldValList[index][textEntry] = newValue
        },
        modifier = Modifier
            .fillMaxWidth()
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
                                focusedLine.value = index+1
                            }
                            true
                        }

                        Key.DirectionUp -> {
                            if (index > 0) {
                                println("up arrow pressed")
                                focusedLine.value = index-1
                            }
                            true
                        }

                        Key.Enter -> {
                            for (entry in fieldState) {
                                println("ENTRIES BEFORE ADD: $entry")
                                println("ENTRY INDEX: $index")
                            }
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
        onTextLayout = { layoutResult ->
            layoutResult.multiParagraph.
            println("TEXT LAYOUT: ${layoutResult.getCursorRect(0).}")

        },
        singleLine = true
    )
    LaunchedEffect(focusedLine.value) {
        if (focusedLine.value == index && index < fieldState.size) {

            try {
                textEntry.focusRequester.requestFocus()
            } catch (e: Exception) {
                // Handle exception gracefully
            }
        }
    }


}