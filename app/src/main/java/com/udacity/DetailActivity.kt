package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_main.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelNotifications()
        val intent = intent
        var repo = ""
        var status = ""
        if (intent.hasExtra("repo") && intent.hasExtra("status")) {
            val extrass = intent.extras
            repo = extrass?.getString("repo").toString()
            status = extrass?.getString("status").toString()
        } else {
            repo = " no thing attached"
            status = "no status found"
        }
        val statusView = findViewById<TextView>(R.id.status)
        statusView.text = status
        val repoView = findViewById<TextView>(R.id.file_name)
        repoView.text = repo
    }

    override fun onResume() {
        super.onResume()

        val motionLayout: MotionLayout = findViewById(R.id.motionLayout)
        motionLayout.transitionToState(R.id.end)

    }

    fun goToHome(view: View) {
        startActivity(Intent(this,MainActivity::class.java))
    }

}
