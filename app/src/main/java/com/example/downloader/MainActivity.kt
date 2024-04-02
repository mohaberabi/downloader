package com.example.downloader

import ButtonState
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.downloader.data.DownloadModel
import com.example.downloader.utils.Constants
import com.example.downloader.utils.Downloader
import com.example.downloader.utils.PermissionHelper
import com.example.downloader.utils.showNoti
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    companion object {
        const val CHANNEL_ID = "channel_id"
        const val DOWNLOAD_PERMISSION_REQUEST_CODE = 100
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 200

        const val permissionGrantedCode = PackageManager.PERMISSION_GRANTED
    }


    private lateinit var testButton: Button
    private lateinit var radioGroup: RadioGroup

    private lateinit var downloader: Downloader


    private val permissionHelper = PermissionHelper(this)
    private lateinit var notificationManager: NotificationManager

    private lateinit var button: LoadingButton
    private var isDone: Boolean = false
    private var downloadId: Long by Delegates.observable(-1L) { _, old, new ->
        if (old != new) {

            downloader.trackDownloadById(
                new,
                ondDone = {
                    isDone = true

                    if (permissionHelper.isNotificationPermissionGranted()) {
                        notificationManager.showNoti(
                            "",
                            this,
                            choosedModel!!.copy(isDone = isDone)
                        )


                    } else {

                        permissionHelper.requestNotificationPermission()
                        Toast.makeText(
                            this,
                            "We Wanted to show  you notifications but u did not allow please allow ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    button.reset()
                },
            )


            // this were it should stop animation , the receiver i tried many times to make
            // use of it it's not working or not doing anything


        }
    }
    private var choosedModel: DownloadModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)




        setContentView(R.layout.activity_main)
        downloader = Downloader(this)



        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createChannel(CHANNEL_ID, "downloader")
        button = findViewById(R.id.customButton)
        button.setState(ButtonState.Clicked)
        radioGroup = findViewById(R.id.radioGroup)


        Constants.downloadItems.forEach {

                download ->
            val radio = RadioButton(this).apply {
                text = download.title
                tag = download

            }
            radioGroup.addView(radio)

        }

        radioGroup.setOnCheckedChangeListener { _, id ->
            val button = findViewById<RadioButton>(id)
            val downloadModel = button.tag as DownloadModel
            choosedModel = downloadModel
        }

        button.setOnClickListener {

            if (choosedModel == null) {
                Toast.makeText(this, "Please choose", Toast.LENGTH_SHORT).show()
            } else {
                button.setState(ButtonState.Loading)
                downloadRequested(choosedModel!!.url)
            }


        }

    }


    private fun downloadRequested(url: String) {
        if (permissionHelper.requiresDownloadPermission()) {
            if (permissionHelper.downloadPermissionAllowed()) {

                startDownload(url)
            } else {
                permissionHelper.requestDownloadPermission()
            }
        } else {


            startDownload(url)
        }
    }


    private fun startDownload(url: String) {
        val id = downloader.download(url)
        downloadId = id
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                getString(R.string.app_description)

            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == DOWNLOAD_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == permissionGrantedCode) {
                startDownload(choosedModel!!.url)
            } else {
                permissionHelper.requestDownloadPermission()
            }
        } else if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == permissionGrantedCode) {
                startDownload(choosedModel!!.url)
            } else {
                permissionHelper.requestNotificationPermission()
            }
        }
    }


}