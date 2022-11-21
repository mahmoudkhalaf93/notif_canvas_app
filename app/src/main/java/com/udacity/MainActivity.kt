package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    var methodNameDownload: String = ""
    var status: String = ""
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    var isDownloadFinished = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createChannel(  getString( R.string.download_notification_channel_id),  getString(R.string.download_notification_channel_name))
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            title = getString(R.string.main_screen_name)
        }

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (methodNameDownload.equals("")) {
                Toast.makeText(this, "please select the file to download", Toast.LENGTH_SHORT)
                    .show()
            } else if(isDownloadFinished) {
                download()
            }
        }

        // on radio buttons
        glide_radio_button.setOnClickListener {
            methodNameDownload = resources.getString(R.string.glide_text)
            URL = "https://codeload.github.com/bumptech/glide/zip/refs/heads/master"
        }
        loadapp_radio_button.setOnClickListener {
            methodNameDownload = resources.getString(R.string.loadapp_text)
            URL =
                "https://codeload.github.com/udacity/nd940-c3-advanced-android-programming-project-starter/zip/refs/heads/master"

        }
        retrofit_radio_button.setOnClickListener {
            methodNameDownload = resources.getString(R.string.retrofit_text)
            URL = "https://codeload.github.com/square/retrofit/zip/refs/heads/master"
        }
        //startActivity(Intent(this,DetailActivity::class.java))
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var methodName = methodNameDownload
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                val notificationManager = ContextCompat.getSystemService(
                    applicationContext,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    applicationContext.getText(R.string.notification_description).toString(),methodNameDownload,status,
                    applicationContext
                )
            }
        }
    }

    private fun download() {

        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            // Thread(Runnable {
            var progress = 0
             isDownloadFinished = false
            custom_button.downloadState = "we are loading"

            while (!isDownloadFinished) {
                var cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
                if (cursor.moveToFirst()) {
                    val downloadStatus =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when (downloadStatus) {
                        DownloadManager.STATUS_RUNNING -> {
                            val totalBytes =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            if (totalBytes > 0) {
                                val downloadedBytes =
                                    cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                progress = (downloadedBytes * 100 / totalBytes).toInt();

                              //  runOnUiThread(Runnable {
                                withContext(Dispatchers.Main){
                                   // seekBar.setProgress(progress)
                                    custom_button.progress = progress
                                }
                              //  })
                            }
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                          //  runOnUiThread(Runnable {
                            status="success"
                                withContext(Dispatchers.Main) {
                                    progress = 100;
                                  //  seekBar.setProgress(progress)
                                    custom_button.downloadState = "download"
                                    custom_button.progress = 0
                                }
                         //   })
                            isDownloadFinished = true;
                        }
                        DownloadManager.STATUS_FAILED -> {
                            status = " failed"
                                withContext(Dispatchers.Main) {
                                    custom_button.downloadState = "download"
                                    custom_button.progress = 0
                                }
                            isDownloadFinished = true;
                        }
                    }
                }
                cursor.close()
            }

            withContext(Dispatchers.Main) {  custom_button.downloadState = "download"
            custom_button.progress = 0}
            //  }).start()
        }

    }

    companion object {
        private var URL = ""
        private const val CHANNEL_ID = "channelId"
    }
    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
        // TODO: Step 1.6 END create a channel
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
