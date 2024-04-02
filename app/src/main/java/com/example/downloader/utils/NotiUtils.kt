package com.example.downloader.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.downloader.DetailActivity
import com.example.downloader.MainActivity
import com.example.downloader.R
import java.io.Serializable

object NotiConst {
    const val NOTIFICATION_ID = 0
    const val EXTRA_KEY = "extra"

}

fun NotificationManager.showNoti(
    body: String,
    appContext: Context,
    extra: Serializable? = null
) {

    if (!areNotificationsEnabled()) {
        return
    }
    val contentIntent = Intent(appContext, DetailActivity::class.java).apply {
        putExtra(NotiConst.EXTRA_KEY, extra)
    }
    val pendingIntent =
        PendingIntent.getActivity(
            appContext,
            NotiConst.NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    val actionIntent = Intent(
        appContext,
        AppNotiReciver::class.java
    )

    val actionPendingIntent = PendingIntent.getBroadcast(
        appContext,
        0,
        actionIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val builder = NotificationCompat.Builder(appContext, MainActivity.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Download Completed")
        .setContentText(if (body.isEmpty()) "Your file has been downloaded" else body)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .addAction(
            R.drawable.ic_launcher_foreground,
            "Open",
            actionPendingIntent
        )

    notify(NotiConst.NOTIFICATION_ID, builder.build())


}