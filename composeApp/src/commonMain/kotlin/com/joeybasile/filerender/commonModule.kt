package com.joeybasile.filerender

import com.joeybasile.filerender.QuickInsert.RichTextState
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    viewModel { FileRenderViewModel() }

    singleOf(::RichTextState)

}