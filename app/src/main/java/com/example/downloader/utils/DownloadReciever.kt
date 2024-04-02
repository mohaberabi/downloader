package com.example.downloader.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DownloadReciever(

    private val downloadListener: DownloadListener
) : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {

            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            downloadListener.onComplete(id)

        }
    }

}


interface DownloadListener {
    fun onComplete(id: Long)
}