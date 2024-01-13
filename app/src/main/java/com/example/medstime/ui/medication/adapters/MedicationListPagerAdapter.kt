package com.example.medstime.ui.medication.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.domain.models.MedicationIntakeModel
import com.example.medstime.ui.medication.MedicationListFragment


class MedicationListPagerAdapter(
    fragment: Fragment,
    private val list: List<MedicationIntakeModel.Date>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = MedicationListFragment()
        fragment.arguments = Bundle().apply {
            putInt("day", list[position].day)
            putInt("month", list[position].month)
            putInt("year", list[position].year)
        }
        return fragment
    }
}