package com.example.medstime.ui.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.medstime.databinding.FragmentMedicationBinding

class MedicationFragment : Fragment() {

    private lateinit var binding: FragmentMedicationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMedicationBinding.inflate(inflater, container, false)
        binding.showCalendar.post { setTopMargin(binding.showCalendar.height) }
        binding.showCalendar.setOnClickListener {
            if (binding.calendar.visibility == View.VISIBLE) {
                binding.calendar.visibility = View.GONE
            } else {
                binding.calendar.visibility = View.VISIBLE
            }
        }
        return binding.root
    }

    private fun setTopMargin(topMargin: Int) {
        val layoutParams = binding.medicationsList.layoutParams as FrameLayout.LayoutParams
        layoutParams.setMargins(0, topMargin, 0, 0)
        binding.medicationsList.layoutParams = layoutParams
    }


}