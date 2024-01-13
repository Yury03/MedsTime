package com.example.medstime.ui.main_activity

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.medstime.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private companion object {
        const val LOG_TAG = "MainActivity"
        private const val NECESSARY_PERMISSIONS_CODE = 100
        private const val SCHEDULE_EXACT_ALARM_PERMISSION_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_medication -> navController.navigate(R.id.medicationFragment)

                R.id.menu_medsTracking -> navController.navigate(R.id.medsTrackingFragment)

                R.id.menu_notifications -> navController.navigate(R.id.notificationsFragment)
            }
            true
        }
        val listenerForBeta = View.OnLongClickListener { _ ->
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            val cameraBeta = sharedPref.getBoolean(getString(R.string.sp_key_camera_beta), false)
            with(sharedPref.edit()) {
                if (!cameraBeta) {
                    putBoolean(getString(R.string.sp_key_camera_beta), true)
                    apply()
                } else {
                    putBoolean(getString(R.string.sp_key_camera_beta), false)
                    apply()
                }
            }
            Toast.makeText(this, R.string.beta_mode_activated, Toast.LENGTH_SHORT).show()
            true
        }
        bottomNavigationView.findViewById<View>(R.id.menu_notifications)
            .setOnLongClickListener(listenerForBeta)
        checkAndRequestNecessaryPermissions()
    }

    private fun checkAndRequestNecessaryPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!requestScheduleExactAlarmPermission()) {
                //TODO:1 Dialog
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        val permissionsNotGranted = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        permissionsNotGranted.forEach { permission ->
            ActivityCompat.requestPermissions(
                this, arrayOf(permission), NECESSARY_PERMISSIONS_CODE
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestScheduleExactAlarmPermission(): Boolean {
        val alarmManager: AlarmManager = ContextCompat.getSystemService(
            this, AlarmManager::class.java
        ) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val settingsIntent = Intent(
                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                Uri.parse("package:$packageName")
            )
            //TODO:2 Dialog
            startActivity(settingsIntent)
        }
        return alarmManager.canScheduleExactAlarms()
    }

    fun hideBottomNavigationBar() {
        findViewById<BottomNavigationView>(R.id.bottomNavigationBar).visibility = View.GONE
        Log.d(LOG_TAG, "hideBottomNavigationBar")
    }

    fun showBottomNavigationBar() {
        findViewById<BottomNavigationView>(R.id.bottomNavigationBar).visibility = View.VISIBLE
        Log.d(LOG_TAG, "showBottomNavigationBar")
    }
}