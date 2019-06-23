package com.student.homeautomationiot.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.student.homeautomationiot.R

import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*

class DashboardActivity : AppCompatActivity() {
    var isAuto: Boolean? = null
    var isFanOn: Boolean? = null
    var isLightOn: Boolean? = null
    var successFetch: Int = 0
    var failureFetch: Int = 0
    var isInitialFetchDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        val db = FirebaseDatabase.getInstance()
        val operatingModeRef = db.getReference("isAuto");
        val fanOnRef = db.getReference("isFanOn");
        val lightOnRef = db.getReference("isLightOn");


        updateToActiveState(false)
        manualControlContainer.visibility = View.GONE

        operatingModeRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.w("Realtime db read failed", "Failed to read value.", p0.toException())
                if (!isInitialFetchDone) {
                    failureFetch += 1
                    checkAndUpdateState()
                }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                isAuto = dataSnapshot.getValue(Boolean::class.java)
                if (!isInitialFetchDone) {
                    successFetch += 1
                    checkAndUpdateState()
                }
                else {
                    updateContent()
                }
                Log.d("isAuto", isAuto.toString());
            }
        })

        fanOnRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.w("Realtime db read failed", "Failed to read value.", p0.toException())
                if (!isInitialFetchDone) {
                    failureFetch += 1
                    checkAndUpdateState()

                }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                isFanOn = dataSnapshot.getValue(Boolean::class.java)
                if (!isInitialFetchDone) {
                    successFetch += 1
                    checkAndUpdateState()
                }
                else {
                    updateContent()
                }
                Log.d("isAuto", isAuto.toString());
            }
        })

        lightOnRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.w("Realtime db read failed", "Failed to read value.", p0.toException())
                if (!isInitialFetchDone) {
                    failureFetch += 1
                    checkAndUpdateState()
                }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                isLightOn = dataSnapshot.getValue(Boolean::class.java)
                if (!isInitialFetchDone) {
                    successFetch += 1
                    checkAndUpdateState()
                }
                else {
                    updateContent()
                }
                Log.d("isAuto", isAuto.toString());
            }
        })

        autoButton.setOnClickListener {
            isAuto?.let { validIsAuto: Boolean ->
                if (!validIsAuto) {
                    operatingModeRef.setValue(true)
                    fanOnRef.setValue(false)
                    lightOnRef.setValue(false)
                }
            }
        }

        manualButton.setOnClickListener {
            isAuto?.let { validIsAuto: Boolean ->
                if (validIsAuto) {
                    operatingModeRef.setValue(false)
                    fanOnRef.setValue(false)
                    lightOnRef.setValue(false)
                }
            }

        }

        lightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            lightOnRef.setValue(isChecked)
        }

        fanSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            fanOnRef.setValue(isChecked)
        }

    }

    fun updateContent( ) {
        if (isAuto != null && isFanOn != null && isLightOn != null) {
            if (isAuto!!) {
                autoButton.setBackgroundResource(R.drawable.ic_auto_enabled)
                manualButton.setBackgroundResource(R.drawable.ic_manual_disabled)
                manualControlContainer.visibility = View.GONE
            }
            else {
                autoButton.setBackgroundResource(R.drawable.ic_auto_disabled)
                manualButton.setBackgroundResource(R.drawable.ic_manual_enabled)
                manualControlContainer.visibility = View.VISIBLE

                if (fanSwitch.isChecked == !isFanOn!!) {
                    fanSwitch.isChecked = isFanOn!!
                }

                if (lightSwitch.isChecked == !isLightOn!!) {
                    lightSwitch.isChecked = isLightOn!!
                }
            }
        }
    }

    @Synchronized
    fun checkAndUpdateState() {
        if ((successFetch + failureFetch) < 3) {
            return
        }

        isInitialFetchDone = true
        if (successFetch == 3) {
            updateToActiveState(true)
            updateContent()
        }
        else {
            updateToActiveState(false)
        }
    }

    fun updateToActiveState(value: Boolean) {
        autoButton.isEnabled = value
        manualButton.isEnabled = value
        autoButton.setBackgroundResource(R.drawable.ic_auto_disabled)
        manualButton.setBackgroundResource(R.drawable.ic_manual_disabled)
    }
}
