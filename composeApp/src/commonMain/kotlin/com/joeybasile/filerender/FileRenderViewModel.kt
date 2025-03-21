package com.joeybasile.filerender

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

/*
class FileRenderViewModel : ViewModel(), KoinComponent {

    private val _uiState = MutableStateFlow(FileRenderUiState())
    val uiState: StateFlow<FileRenderUiState> = _uiState.asStateFlow()

    private val defaultJson = """{"document":{ "title":"", "content": "" }}"""

    init {
        setInputJson(defaultJson)
    }

    /** Sets the input JSON and updates the UI state with parsed document or error state */
    fun setInputJson(json: String) {
        try {
            val root = Json.decodeFromString<RootJson>(json)
            val documentJson = root.document
            val title = documentJson.title
            val content = parseContent(documentJson.content)
            _uiState.value = FileRenderUiState(
                inputJson = json,
                documentJson = documentJson, // Store the raw document data
                document = Document(title, content),
                isInvalid = false
            )
        } catch (e: Exception) {
            _uiState.value = FileRenderUiState(
                inputJson = json,
                documentJson = null,
                document = null,
                isInvalid = true
            )
        }
    }

    /** Updates the title and regenerates the JSON */
    fun updateTitle(newTitle: String) {
        val currentDocumentJson = _uiState.value.documentJson ?: return
        val updatedDocumentJson = currentDocumentJson.copy(title = newTitle)
        val root = RootJson(updatedDocumentJson)
        val newJson = Json.encodeToString(root)
        _uiState.value = _uiState.value.copy(
            inputJson = newJson,
            documentJson = updatedDocumentJson,
            document = Document(newTitle, _uiState.value.document?.content ?: AnnotatedString(""))
        )
    }

    /** Updates the content and regenerates the JSON */
    fun updateContent(newContent: String) {
        val currentDocumentJson = _uiState.value.documentJson ?: return
        val updatedDocumentJson = currentDocumentJson.copy(content = newContent)
        val root = RootJson(updatedDocumentJson)
        val newJson = Json.encodeToString(root)
        val parsedContent = parseContent(newContent)
        _uiState.value = _uiState.value.copy(
            inputJson = newJson,
            documentJson = updatedDocumentJson,
            document = Document(_uiState.value.document?.title ?: "", parsedContent)
        )
    }

    /** Resets the input to the default JSON */
    fun resetToDefault() {
        setInputJson(defaultJson)
    }

    /** Parses the content string into an AnnotatedString with styles for bold, italic, and underline */
    private fun parseContent(content: String): AnnotatedString {
        val builder = AnnotatedString.Builder()
        var currentIndex = 0
        val supportedTags = setOf("bold", "underline", "italic")

        while (currentIndex < content.length) {
            val nextTagStart = content.indexOf('<', currentIndex)
            if (nextTagStart == -1) {
                builder.append(content.substring(currentIndex))
                break
            }

            builder.append(content.substring(currentIndex, nextTagStart))

            val tagEnd = content.indexOf('>', nextTagStart)
            if (tagEnd == -1) {
                builder.append(content.substring(nextTagStart))
                break
            }

            val tagName = content.substring(nextTagStart + 1, tagEnd)
            if (tagName in supportedTags) {
                val closingTag = "</$tagName>"
                val tagContentStart = tagEnd + 1
                val tagContentEnd = content.indexOf(closingTag, tagContentStart)

                if (tagContentEnd == -1) {
                    builder.append(content.substring(nextTagStart))
                    break
                }

                val tagContent = content.substring(tagContentStart, tagContentEnd)
                when (tagName) {
                    "bold" -> builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(tagContent)
                    }
                    "italic" -> builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(tagContent)
                    }
                    "underline" -> builder.withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(tagContent)
                    }
                }
                currentIndex = tagContentEnd + closingTag.length
            } else {
                builder.append(content.substring(nextTagStart, tagEnd + 1))
                currentIndex = tagEnd + 1
            }
        }
        return builder.toAnnotatedString()
    }
}

/** UI state holding the input JSON, raw document data, parsed document, and validity */
data class FileRenderUiState(
    val inputJson: String = "",
    val documentJson: DocumentJson? = null, // Added to store raw title and content
    val document: Document? = null,
    val isInvalid: Boolean = false
)

/** Represents the parsed document with title and styled content */
data class Document(
    val title: String,
    val content: AnnotatedString
)

/** JSON structure for deserialization */
@Serializable
data class DocumentJson(
    val title: String,
    val content: String
)

@Serializable
data class RootJson(
    val document: DocumentJson
)
*/
class FileRenderViewModel: ViewModel(), KoinComponent {

    private val _uiState = MutableStateFlow(FileRenderUiState())
    val uiState: StateFlow<FileRenderUiState> = _uiState.asStateFlow()

    private val defaultJson = """{"document":{ "title":"", "content": "" }}"""

    init {
        // Initialize with default JSON
        setInputJson(defaultJson)
    }

    /** Sets the input JSON and updates the UI state with parsed document or error state */
    fun setInputJson(json: String) {
        try {
            val root = Json.decodeFromString<RootJson>(json)
            val documentJson = root.document
            val title = documentJson.title
            val content = parseContent(documentJson.content)
            _uiState.value = FileRenderUiState(
                inputJson = json,
                document = Document(title, content),
                isInvalid = false
            )
        } catch (e: Exception) {
            _uiState.value = FileRenderUiState(
                inputJson = json,
                document = null,
                isInvalid = true
            )
        }
    }

    /** Resets the input to the default JSON */
    fun resetToDefault() {
        setInputJson(defaultJson)
    }

    /** Parses the content string into an AnnotatedString with styles for bold, italic, and underline */
    private fun parseContent(content: String): AnnotatedString {
        val builder = AnnotatedString.Builder()
        var currentIndex = 0
        val supportedTags = setOf("bold", "underline", "italic")

        while (currentIndex < content.length) {
            val nextTagStart = content.indexOf('<', currentIndex)
            if (nextTagStart == -1) {
                builder.append(content.substring(currentIndex))
                break
            }

            // Append text before the tag
            builder.append(content.substring(currentIndex, nextTagStart))

            // Find tag name
            val tagEnd = content.indexOf('>', nextTagStart)
            if (tagEnd == -1) {
                builder.append(content.substring(nextTagStart))
                break
            }

            val tagName = content.substring(nextTagStart + 1, tagEnd)
            if (tagName in supportedTags) {
                val closingTag = "</$tagName>"
                val tagContentStart = tagEnd + 1
                val tagContentEnd = content.indexOf(closingTag, tagContentStart)

                if (tagContentEnd == -1) {
                    builder.append(content.substring(nextTagStart))
                    break
                }

                val tagContent = content.substring(tagContentStart, tagContentEnd)
                when (tagName) {
                    "bold" -> builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(tagContent)
                    }
                    "italic" -> builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(tagContent)
                    }
                    "underline" -> builder.withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(tagContent)
                    }
                }
                currentIndex = tagContentEnd + closingTag.length
            } else {
                // Treat unsupported tags as plain text
                builder.append(content.substring(nextTagStart, tagEnd + 1))
                currentIndex = tagEnd + 1
            }
        }
        return builder.toAnnotatedString()
    }
}



/** UI state holding the input JSON and parsed document */
data class FileRenderUiState(
    val inputJson: String = "",
    val document: Document? = null,
    val isInvalid: Boolean = false
)

/** Represents the parsed document with title and styled content */
data class Document(
    val title: String,
    val content: AnnotatedString
)

/** JSON structure for deserialization */
@Serializable
data class DocumentJson(
    val title: String,
    val content: String
)

@Serializable
data class RootJson(
    val document: DocumentJson
)

