package com.example.downloader.utils

import com.example.downloader.data.DownloadModel

object Constants {


    val downloadItems = listOf<DownloadModel>(

        DownloadModel(
            0,
            "https://github.com/bumptech/glide",
            "Glide-Image Loading Library By BumpTech",
            false
        ),
        DownloadModel(
            1,
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter",
            "LoadApp-Current Repository by Udacity",
            false
        ),
        DownloadModel(
            2,
            "https://github.com/square/retrofit",
            "Retrofit - Type-safe HTTP client for Android and Java by Square,Inc",
            false
        )
    )
}