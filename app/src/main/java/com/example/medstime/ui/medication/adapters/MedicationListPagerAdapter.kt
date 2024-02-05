package com.example.medstime.ui.medication.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.domain.models.MedicationIntakeModel
import com.example.medstime.ui.medication.MedicationListFragment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class MedicationListPagerAdapter(
    fragment: Fragment,
    private val list: List<MedicationIntakeModel.Date>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = MedicationListFragment()
        val dateString = Json.encodeToString<MedicationIntakeModel.Date>(
            MedicationIntakeModel.Date(
                day = list[position].day,
                month = list[position].month,
                year = list[position].year
            )
        )
        fragment.arguments = Bundle().apply {
            putString("date", dateString)
        }
        return fragment
    }
}