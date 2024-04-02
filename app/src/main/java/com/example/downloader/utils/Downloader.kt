package com.example.downloader.utils

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper

class Downloader(context: Context) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)


    fun download(url: String): Long {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "downloaderApp"
                )


        return downloadManager.enqueue(request)
    }


    fun trackDownloadById(
        id: Long,
        onFailed: () -> Unit = {},
        onPause: () -> Unit = {},
        onPending: () -> Unit = {},
        onRunning: () -> Unit = {},
        ondDone: () -> Unit = {},

        ) {
        val handler = Handler(Looper.getMainLooper())

        var isDownloaded = false
        while (!isDownloaded) {
            val curser = downloadManager.query(DownloadManager.Query().setFilterById(id))
            if (curser.moveToFirst()) {

                val index = curser.getColumnIndex(DownloadManager.COLUMN_STATUS)
                if (index < 0) {
                    return
                }

                val status = curser.getInt(index)


                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        isDownloaded = true
                        onFailed.invoke()
                    }

                    DownloadManager.STATUS_PENDING -> onPending.invoke()
                    DownloadManager.STATUS_RUNNING -> onRunning.invoke()
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        handler.postDelayed({
                            ondDone.invoke()
                        }, 3000)
                        isDownloaded = true
                    }

                }
            }
        }
    }

}