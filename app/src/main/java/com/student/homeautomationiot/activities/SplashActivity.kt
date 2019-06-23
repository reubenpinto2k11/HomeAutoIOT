package com.student.homeautomationiot.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.student.homeautomationiot.R

class SplashActivity : AppCompatActivity() {
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        runnable = Runnable {
            val intentToDashboardActivity= Intent(this, DashboardActivity::class.java)
            startActivity(intentToDashboardActivity)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        handler?.postDelayed(runnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler?.removeCallbacks(runnable)
    }

}
