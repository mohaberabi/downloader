package com.example.downloader.data

import java.io.Serializable

data class DownloadModel(
    val id: Int,
    val url: String,
    val title: String,
    val isDone: Boolean
) : Serializable
