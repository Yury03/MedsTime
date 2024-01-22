package com.example.medstime.ui.main_activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.medstime.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavBar = getBottomNavBar(navController)

        setNavigationListener(navController, bottomNavBar)
        setBottomNavBarListener(navController, bottomNavBar)
        setBetaListener(bottomNavBar)
        checkAndRequestNecessaryPermissions()
    }

    private fun getBottomNavBar(navController: NavController) =
        findViewById<BottomNavigationView>(R.id.bottomNavigationBar).apply {
            setupWithNavController(navController)
        }

    private fun setBetaListener(bottomNavBar: BottomNavigationView) {
        val listenerForBeta = View.OnLongClickListener { _ ->
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            val cameraBeta = sharedPref.getBoolean(getString(R.string.sp_key_camera_beta), false)
            with(sharedPref.edit()) {
                if (!cameraBeta) {
                    putBoolean(getString(R.string.sp_key_camera_beta), true)
                } else {
                    putBoolean(getString(R.string.sp_key_camera_beta), false)
                }
                apply()
            }
            Toast.makeText(this, R.string.beta_mode_activated, Toast.LENGTH_SHORT).show()
            true
        }
        bottomNavBar.findViewById<View>(R.id.notificationsFragment)
            .setOnLongClickListener(listenerForBeta)
    }

    private fun setBottomNavBarListener(
        navController: NavController,
        bottomNavBar: BottomNavigationView,
    ) {
        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.medicationFragment -> navController.navigate(R.id.medicationFragment)

                R.id.medsTrackingFragment -> navController.navigate(R.id.medsTrackingFragment)

                R.id.notificationsFragment -> navController.navigate(R.id.notificationsFragment)
            }
            true
        }
    }

    private fun setNavigationListener(
        navController: NavController,
        bottomNavBar: BottomNavigationView,
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.medicationFragment, R.id.medsTrackingFragment, R.id.notificationsFragment -> showBottomNavigationBar(
                    bottomNavBar
                )

                R.id.addMedTrackFragment, R.id.addMedFragment -> hideBottomNavigationBar(
                    bottomNavBar
                )
            }
        }
    }

    private fun checkAndRequestNecessaryPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE)
        }/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!requestScheduleExactAlarmPermission()) {
                //TODO:1 Dialog
            }
        }*/
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

//    @RequiresApi(Build.VERSION_CODES.S)
//    private fun requestScheduleExactAlarmPermission(): Boolean {
//        val alarmManager: AlarmManager = ContextCompat.getSystemService(
//            this, AlarmManager::class.java
//        ) as AlarmManager
//        if (!alarmManager.canScheduleExactAlarms()) {
//            val settingsIntent = Intent(
//                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
//                Uri.parse("package:$packageName")
//            )
//            //TODO:2 Dialog
//            startActivity(settingsIntent)
//        }
//        return alarmManager.canScheduleExactAlarms()
//    }

    private fun hideBottomNavigationBar(bottomNavBar: BottomNavigationView) {
        Log.d(LOG_TAG, "hideBottomNavigationBar")
        with(bottomNavBar) {
            if (visibility == View.VISIBLE) visibility = View.GONE
        }
    }

    private fun showBottomNavigationBar(bottomNavBar: BottomNavigationView) {
        with(bottomNavBar) {
            if (visibility == View.GONE) visibility = View.VISIBLE
        }
        Log.d(LOG_TAG, "showBottomNavigationBar")
    }

    private companion object {

        const val LOG_TAG = "MainActivity"
        private const val NECESSARY_PERMISSIONS_CODE = 100
//        private const val SCHEDULE_EXACT_ALARM_PERMISSION_CODE = 200
    }
}