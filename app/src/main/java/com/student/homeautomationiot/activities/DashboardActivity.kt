package com.student.homeautomationiot.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
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
    var isLight1On: Boolean? = null
    var isLight2On: Boolean? = null
    var humidity: Int? = null
    var temperature: Int? = null
    var luminousIntensity: Int? = null
    var successFetch: Int = 0
    var failureFetch: Int = 0
    var isInitialFetchDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        val db = FirebaseDatabase.getInstance()
        val operatingModeRef = db.getReference("isAuto");
        val fan1OnRef = db.getReference("isFan1On");
        val light2OnRef = db.getReference("isLight2On");
        val light1OnRef = db.getReference("isLight1On");
        val tempRef = db.getReference("temperature");
        val humidityRef = db.getReference("humidity");
        val intensityRef = db.getReference("lumens");


        updateToActiveState(false)
        manualControlContainer.visibility = View.GONE
        roomStatsContainer.visibility = View.GONE

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

        fan1OnRef.addValueEventListener(object : ValueEventListener {
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
            }
        })

        light1OnRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.w("Realtime db read failed", "Failed to read value.", p0.toException())
                if (!isInitialFetchDone) {
                    failureFetch += 1
                    checkAndUpdateState()
                }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                isLight1On = dataSnapshot.getValue(Boolean::class.java)
                if (!isInitialFetchDone) {
                    successFetch += 1
                    checkAndUpdateState()
                }
                else {
                    updateContent()
                }
            }
        })

        light2OnRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.w("Realtime db read failed", "Failed to read value.", p0.toException())
                if (!isInitialFetchDone) {
                    failureFetch += 1
                    checkAndUpdateState()
                }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                isLight2On = dataSnapshot.getValue(Boolean::class.java)
                if (!isInitialFetchDone) {
                    successFetch += 1
                    checkAndUpdateState()
                }
                else {
                    updateContent()
                }
            }
        })

        tempRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.w("Realtime db read failed", "Failed to read value.", p0.toException())
                if (!isInitialFetchDone) {
                    failureFetch += 1
                    checkAndUpdateState()
                }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                temperature = dataSnapshot.getValue(Int::class.java)
                if (!isInitialFetchDone) {
                    successFetch += 1
                    checkAndUpdateState()
                }
                else {
                    updateContent()
                }
            }
        })

        humidityRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.w("Realtime db read failed", "Failed to read value.", p0.toException())
                if (!isInitialFetchDone) {
                    failureFetch += 1
                    checkAndUpdateState()
                }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                humidity = dataSnapshot.getValue(Int::class.java)
                if (!isInitialFetchDone) {
                    successFetch += 1
                    checkAndUpdateState()
                }
                else {
                    updateContent()
                }
            }
        })

        intensityRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.w("Realtime db read failed", "Failed to read value.", p0.toException())
                if (!isInitialFetchDone) {
                    failureFetch += 1
                    checkAndUpdateState()
                }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                luminousIntensity = dataSnapshot.getValue(Int::class.java)
                if (!isInitialFetchDone) {
                    successFetch += 1
                    checkAndUpdateState()
                }
                else {
                    updateContent()
                }
            }
        })
        autoButton.setOnClickListener {
            isAuto?.let { validIsAuto: Boolean ->
                if (!validIsAuto) {
                    operatingModeRef.setValue(true)
                    fan1OnRef.setValue(false)
                    light1OnRef.setValue(false)
                    light2OnRef.setValue(false)
                    tempRef.setValue(0)
                    humidityRef.setValue(0)
                    intensityRef.setValue(0)
                }
            }
        }

        manualButton.setOnClickListener {
            isAuto?.let { validIsAuto: Boolean ->
                if (validIsAuto) {
                    operatingModeRef.setValue(false)
                }
            }

        }

        btnTurnAllOff.setOnClickListener {
            fan1OnRef.setValue(false)
            light1OnRef.setValue(false)
            light2OnRef.setValue(false)
        }

        btnTurnAllOn.setOnClickListener {
            fan1OnRef.setValue(true)
            light1OnRef.setValue(true)
            light2OnRef.setValue(true)
        }

        light1Switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            light1OnRef.setValue(isChecked)
        }

        light2Switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            light2OnRef.setValue(isChecked)
        }

        fan1Switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            fan1OnRef.setValue(isChecked)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        if (item?.itemId == R.id.action_settings) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                finish()
            }
        }
        return true
    }

    fun updateContent( ) {
        if (isAuto != null && isFanOn != null && isLight1On != null && isLight2On != null && humidity != null && temperature != null && luminousIntensity != null) {
            if (isAuto!!) {
                autoButton.setBackgroundResource(R.drawable.ic_auto_enabled)
                manualButton.setBackgroundResource(R.drawable.ic_manual_disabled)
                manualControlContainer.visibility = View.GONE
                roomStatsContainer.visibility = View.VISIBLE
            }
            else {
                autoButton.setBackgroundResource(R.drawable.ic_auto_disabled)
                manualButton.setBackgroundResource(R.drawable.ic_manual_enabled)
                manualControlContainer.visibility = View.VISIBLE
                roomStatsContainer.visibility = View.GONE

                if (fan1Switch.isChecked == !isFanOn!!) {
                    fan1Switch.isChecked = isFanOn!!
                }

                if (light1Switch.isChecked == !isLight1On!!) {
                    light1Switch.isChecked = isLight1On!!
                }

                if (light2Switch.isChecked == !isLight2On!!) {
                    light2Switch.isChecked = isLight2On!!
                }
            }

            humidity?.let {
                lblHumidityValue.text = getString(R.string.humidity_format, it)
            }

            temperature?.let {
                lblTempValue.text = getString(R.string.temp_format, it)
            }

            luminousIntensity?.let {
                lblLightIntensityValue.text = getString(R.string.intensity_format, it)
            }
        }
    }

    @Synchronized
    fun checkAndUpdateState() {
        if ((successFetch + failureFetch) < 6) {
            return
        }

        isInitialFetchDone = true
        if (successFetch == 6) {
            updateToActiveState(true)
            updateContent()
        }
        else {
            updateToActiveState(false)
        }
    }

    private fun updateToActiveState(value: Boolean) {
        autoButton.isEnabled = value
        manualButton.isEnabled = value
        autoButton.setBackgroundResource(R.drawable.ic_auto_disabled)
        manualButton.setBackgroundResource(R.drawable.ic_manual_disabled)
    }
}
