package com.joeybasile.filerender

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform