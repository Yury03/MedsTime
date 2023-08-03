package com.example.medstime.ui.medication

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationBinding
import com.example.medstime.domain.models.MedicationIntakeModel
import com.example.medstime.ui.medication.adapters.TimesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicationFragment : Fragment(R.layout.fragment_medication) {

    private var _binding: FragmentMedicationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MedicationViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicationBinding.bind(view)
        binding.calendar.visibility = View.GONE//todo баг calendarView
        viewModel.setDate()

        binding.showCalendar.post { setTopMargin(binding.showCalendar.height) }

        binding.showCalendar.setOnClickListener { changeVisible(false) }
        binding.hideCalendar.setOnClickListener { changeVisible(true) }

        binding.medicationsList.layoutManager = LinearLayoutManager(context)
        val medicationClick = { it: MedicationIntakeModel ->
            showAlertDialog()
        }
        viewModel.intakeListToday.observe(viewLifecycleOwner) {
            binding.medicationsList.adapter =
                context?.let { it1 -> TimesListAdapter(it, it1, medicationClick) }
        }


        viewModel.currentDate.observe(viewLifecycleOwner) { binding.showCalendar.text = it.first }
    }

    private fun changeVisible(calendarIsVisible: Boolean) {
        if (calendarIsVisible) {
            binding.calendar.visibility = View.GONE
            binding.showCalendar.visibility = View.VISIBLE
            binding.hideCalendar.visibility = View.GONE
        } else {
            binding.calendar.visibility = View.VISIBLE
            binding.showCalendar.visibility = View.GONE
            binding.hideCalendar.visibility = View.VISIBLE
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

    private fun showAlertDialog() {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("AlertDialog Title")
        alertDialogBuilder.setMessage("This is the message of the AlertDialog.")
        alertDialogBuilder.setView(R.layout.medication_alert_dialog)
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            // Код, который будет выполнен при нажатии на кнопку Cancel
            dialog.dismiss() // Закрыть AlertDialog
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }
}