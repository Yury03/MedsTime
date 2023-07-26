package com.example.medstime.ui.main_activity

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.medstime.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private var popupMenuIsOpen: Boolean = false
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_medication ->
                    navController.navigate(R.id.medicationFragment)

                R.id.menu_medsTracking ->
                    navController.navigate(R.id.medsTrackingFragment)

                R.id.menu_add_item -> {
                    if (!popupMenuIsOpen) {
                        showAddItemAlertDialog(bottomNavigation.height)
                    }
                    popupMenuIsOpen = !popupMenuIsOpen
                }

                R.id.menu_notifications ->
                    navController.navigate(R.id.notificationsFragment)
//                R.id.menu_profile -> {
//                    false
//                }

            }
            true
        }
    }

    private fun showAddItemAlertDialog(navBarHeight: Int) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.popup_add_item_menu, null)
        val popupWindow = PopupWindow(
            customView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val animation = AnimationUtils.loadAnimation(this, R.anim.popum_anim)
        popupWindow.animationStyle = android.R.style.Animation_Dialog

        val buttonOption1 = customView.findViewById<Button>(R.id.button_option1)
        val buttonOption2 = customView.findViewById<Button>(R.id.button_option2)
        buttonOption1.setOnClickListener {
            //vm.addMedicationItem()
            popupWindow.dismiss()
        }

        buttonOption2.setOnClickListener {
            //vm.addMedsTrackingItem()
            popupWindow.dismiss()
        }

        customView.setPadding(0, 0, 0, navBarHeight)
        popupWindow.showAtLocation(
            findViewById(R.id.menu_add_item),
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )
        customView.startAnimation(animation)
    }

}