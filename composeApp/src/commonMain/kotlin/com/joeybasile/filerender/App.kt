package com.joeybasile.filerender

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.joeybasile.filerender.QuickInsert.BasicEditor
import com.joeybasile.filerender.QuickInsert.DocEditor
import com.joeybasile.filerender.QuickInsert.RichTextEditor
import com.joeybasile.filerender.QuickInsert.WYSIWYGEditor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import filerender.composeapp.generated.resources.Res
import filerender.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {

        DocEditor()

    //BasicEditor()
        //WYSIWYGEditor()
    //RichTextEditor()
    //FileRenderScreen()
    }
}