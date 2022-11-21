package com.udacity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
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

    fun goToHome(view: View) {
        startActivity(Intent(this,MainActivity::class.java))
    }

}
