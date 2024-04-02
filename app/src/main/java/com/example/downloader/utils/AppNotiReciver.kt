package com.example.downloader.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.downloader.DetailActivity
import com.example.downloader.data.DownloadModel

class AppNotiReciver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {


        if (context != null && intent != null) {
            val downloadModel =
                intent.getSerializableExtra(NotiConst.EXTRA_KEY)

            val detailIntent = Intent(context, DetailActivity::class.java).apply {
                putExtra(NotiConst.EXTRA_KEY, downloadModel)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }
            context.startActivity(detailIntent)

        }
    }
}