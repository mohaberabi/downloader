package com.example.downloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.downloader.data.DownloadModel
import com.example.downloader.utils.NotiConst

class DetailActivity : AppCompatActivity() {

    private lateinit var status: TextView
    private lateinit var fileName: TextView
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        status = findViewById(R.id.statusText)
        fileName = findViewById(R.id.fileName)
        button = findViewById(R.id.button)
        val download = intent.getSerializableExtra(NotiConst.EXTRA_KEY) as DownloadModel?

        status.text = if (download != null && download.isDone) "Succuess" else "Failure"
        fileName.text = download?.title ?: ""
        button.setOnClickListener {
            finish()
        }

    }
}