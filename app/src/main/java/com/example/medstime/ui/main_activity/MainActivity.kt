package com.example.medstime.ui.main_activity

import android.os.Bundle
import android.view.View
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
        findViewById<BottomNavigationView>(R.id.bottomNavigationBar).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_medication -> navController.navigate(R.id.medicationFragment)

                R.id.menu_medsTracking -> navController.navigate(R.id.medsTrackingFragment)

                R.id.menu_notifications -> navController.navigate(R.id.notificationsFragment)
            }
            true
        }
    }

    fun hideBottomNavigationBar() {
        findViewById<BottomNavigationView>(R.id.bottomNavigationBar).visibility = View.GONE
    }

    fun showBottomNavigationBar() {
        findViewById<BottomNavigationView>(R.id.bottomNavigationBar).visibility = View.VISIBLE
    }
}