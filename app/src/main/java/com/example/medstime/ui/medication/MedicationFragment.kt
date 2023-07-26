package com.example.medstime.ui.medication

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationBinding

class MedicationFragment : Fragment(R.layout.fragment_medication) {

    //    private lateinit var binding: FragmentMedicationBinding
    private var _binding: FragmentMedicationBinding? = null
    private val binding get() = _binding!!

    //    private val vm = ViewModelProvider(this)[MainViewModel::class.java]


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicationBinding.bind(view)
//        binding.calendar.visibility = View.GONE
        binding.showCalendar.post { setTopMargin(binding.showCalendar.height) }
        binding.showCalendar.setOnClickListener {
            binding.calendar.visibility =
                if (binding.calendar.visibility == View.VISIBLE) View.GONE else View.VISIBLE //TODO
        }
    }

    private fun setTopMargin(topMargin: Int) {//задает отступ сверху для списка
        val layoutParams = binding.medicationsList.layoutParams as FrameLayout.LayoutParams
        layoutParams.setMargins(0, topMargin, 0, 0)
        binding.medicationsList.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}