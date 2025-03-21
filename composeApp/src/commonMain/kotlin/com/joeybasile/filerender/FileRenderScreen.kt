package com.joeybasile.filerender

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
/*
@Composable
fun FileRenderScreen(
    modifier: Modifier = Modifier
) {
    val fileRenderViewModel: FileRenderViewModel = koinViewModel()
    val uiState by fileRenderViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column: JSON Input
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Input JSON",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextField(
                    value = uiState.inputJson,
                    onValueChange = { newValue ->
                        fileRenderViewModel.setInputJson(newValue)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = { Text("Enter JSON here") },
                    isError = uiState.isInvalid
                )
                Button(
                    onClick = { fileRenderViewModel.resetToDefault() },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text("Reset to Default")
                }
            }

            // Right Column: Document Editor and Rendered Output
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Document Editor",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                if (uiState.documentJson != null) {
                    TextField(
                        value = uiState.documentJson!!.title,
                        onValueChange = { newTitle -> fileRenderViewModel.updateTitle(newTitle) },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = uiState.documentJson!!.content,
                        onValueChange = { newContent -> fileRenderViewModel.updateContent(newContent) },
                        label = { Text("Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Takes available vertical space
                    )
                    Text(
                        text = "Rendered Output",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    if (uiState.document != null) {
                        Text(
                            text = uiState.document!!.title,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = uiState.document!!.content,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                } else {
                    Text(
                        text = "Invalid JSON",
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
*/

@Composable
fun FileRenderScreen(
    modifier: Modifier = Modifier
) {
    val fileRenderViewModel: FileRenderViewModel = koinViewModel()
    val uiState by fileRenderViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column: JSON Input
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Input JSON",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextField(
                    value = uiState.inputJson,
                    onValueChange = { newValue ->
                        fileRenderViewModel.setInputJson(newValue)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Allows the TextField to expand vertically
                    placeholder = { Text("Enter JSON here") },
                    isError = uiState.isInvalid
                )
                Button(
                    onClick = { fileRenderViewModel.resetToDefault() },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text("Reset to Default")
                }
            }

            // Right Column: Rendered Output
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Rendered Output",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                if (uiState.isInvalid) {
                    Text(
                        text = "Invalid JSON",
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else if (uiState.document != null) {
                    Text(
                        text = uiState.document!!.title,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = uiState.document!!.content,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .weight(1f) // Allows content to take remaining space
                    )
                }
            }
            //to test basictextfield
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .border(1.dp, Color.Black)
            ) {/*
                Text(
                    text = "Basic Text Field",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                */
                val textFieldState = rememberTextFieldState("This is the content of the file! What an example, huh?")
                val styleMap = remember { mutableStateOf(mapOf("BOLD" to "0:1")) }
                textFieldState.edit {
                    replace(0, 1, "This is the content of the file! What an example, huh?")
                }
                BasicTextField(
                    state = textFieldState,

                )
                /*
                val styleMap = remember { mutableStateOf(mapOf("BOLD" to listOf("0:1"))) }

                BasicTextField(
                    state = textFieldState,
                    outputTransformation = {
                        // 'this' is TextFieldBuffer here
                        styleMap.value["BOLD"]?.forEach { rangeStr ->
                            val (start, end) = rangeStr.split(":").map { it.toInt() }
                            this.setSpanStyle(
                                SpanStyle(fontWeight = FontWeight.Bold),
                                start,
                                end.coerceAtMost(this.length)
                            )
                        }
                    }
                )

                 */
            }

        }
    }
}


