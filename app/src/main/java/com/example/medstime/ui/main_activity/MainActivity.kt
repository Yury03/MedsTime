package com.example.medstime.ui.main_activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.medstime.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

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
            Toast.makeText(applicationContext, R.string.beta_mode_activated, Toast.LENGTH_SHORT)
            .show()
            true
        }
        bottomNavigationView.findViewById<View>(R.id.menu_notifications)
            .setOnLongClickListener(listenerForBeta)
    }

    fun hideBottomNavigationBar() {
        findViewById<BottomNavigationView>(R.id.bottomNavigationBar).visibility = View.GONE
    }

    fun showBottomNavigationBar() {
        findViewById<BottomNavigationView>(R.id.bottomNavigationBar).visibility = View.VISIBLE
    }
}