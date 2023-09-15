package com.example.medstime.ui.medication

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.MedicationIntakeModel
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationBinding
import com.example.medstime.ui.medication.adapters.TimesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date
import java.util.concurrent.TimeUnit

class MedicationFragment : Fragment(R.layout.fragment_medication) {
    companion object {
        const val MAX_NUMBER_DAYS: Long = 14
        const val MIN_NUMBER_DAYS: Long = 14
    }

    private var _binding: FragmentMedicationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MedicationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicationBinding.bind(view)
        viewModel.setCurrentDate()
        with(binding) {
            calendar.visibility = View.GONE//todo баг calendarView
            medicationsList.layoutManager = LinearLayoutManager(context)
            showCalendar.post { setTopMargin(binding.showCalendar.height) }
            showCalendar.setOnClickListener { changeVisible(false) }
            hideCalendar.setOnClickListener { changeVisible(true) }
            calendar.setOnDateChangeListener { _, _, month, dayOfMonth ->
                viewModel.getIntakeListWithDate(
                    date = MedicationIntakeModel.Date(dayOfMonth, month + 1)
                )//todo
                hideCalendar.callOnClick()
            }
            val maxDate = TimeUnit.DAYS.toMillis(MAX_NUMBER_DAYS)
            val minDate = TimeUnit.DAYS.toMillis(MIN_NUMBER_DAYS)
//            calendar.minDate = (Date().time - minDate)
            calendar.maxDate = (Date().time + maxDate)
            addNewMedication.setOnClickListener {
                val navHostFragment =
                    requireActivity().supportFragmentManager
                        .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                navHostFragment.navController.navigate(R.id.addMedFragment)
            }
        }

        val medicationClick = { model: MedicationIntakeModel,
                                timeAndDosageText: String ->
//            showAlertDialog(it, timeAndDosageText)
            MedicationClickAlert(requireContext(), model, timeAndDosageText).show()
        }
        viewModel.intakeListToday.observe(viewLifecycleOwner) {
            binding.medicationsList.adapter =
                context?.let { context ->
                    TimesListAdapter(it, context, medicationClick)
                }
        }
        viewModel.currentDate.observe(viewLifecycleOwner) { binding.showCalendar.text = it }

    }

    private fun changeVisible(calendarIsVisible: Boolean) {
        with(binding) {
            if (calendarIsVisible) {
                calendar.visibility = View.GONE
                showCalendar.visibility = View.VISIBLE
                hideCalendar.visibility = View.GONE
            } else {
                calendar.visibility = View.VISIBLE
                showCalendar.visibility = View.GONE
                hideCalendar.visibility = View.VISIBLE
            }
        }
    }

    private fun setTopMargin(topMargin: Int) {
        val layoutParams = binding.medicationsList.layoutParams as FrameLayout.LayoutParams
        layoutParams.setMargins(0, topMargin, 0, 0)
        binding.medicationsList.layoutParams = layoutParams
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}