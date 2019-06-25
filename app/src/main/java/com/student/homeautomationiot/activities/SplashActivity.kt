package com.student.homeautomationiot.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.student.homeautomationiot.R

class SplashActivity : AppCompatActivity() {
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var isInitialLoad = true

    private val RC_SIGN_IN: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        runnable = Runnable {

            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

            if (FirebaseAuth.getInstance().currentUser != null) {
                val intentToDashboardActivity= Intent(this, DashboardActivity::class.java)
                startActivity(intentToDashboardActivity)
                finish()
            }
            else {
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.mipmap.ic_launcher)
                        .setTheme(R.style.AppTheme)
                        .build(),RC_SIGN_IN)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (isInitialLoad) {
            isInitialLoad = false
            handler?.postDelayed(runnable, 3000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(runnable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val resp = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                val intentToDashboardActivity= Intent(this, DashboardActivity::class.java)
                startActivity(intentToDashboardActivity)
                finish()
            }
            else {
                if (resp == null) {
                    finish()
                }
                else {
                    resp?.let {
                        Log.d("Error occurred", it.error?.localizedMessage)
                    }
                }
            }
        }
    }

}
