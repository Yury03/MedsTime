package com.example.medstime.ui.medication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationBinding
import com.example.medstime.ui.medication.adapters.TimesListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class MedicationFragment : Fragment(R.layout.fragment_medication) {

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
                    com.example.domain.models.MedicationIntakeModel.Date(
                        dayOfMonth,
                        month + 1
                    )
                )//todo

                hideCalendar.callOnClick()
            }
            val maxDate = (1000 * 3600 * 24 * 14)
            val minDate = (1000 * 3600 * 24 * 14)
//            calendar.minDate = (Date().time - minDate)
            calendar.maxDate = (Date().time + maxDate)
        }

        val medicationClick = { model: com.example.domain.models.MedicationIntakeModel,
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


    @SuppressLint("UseCompatLoadingForDrawables", "InflateParams")//TODO
    private fun showAlertDialog(
        medicationIntakeModel: com.example.domain.models.MedicationIntakeModel,
        timeAndDosageText: String
    ) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        val customLayout: View = layoutInflater.inflate(R.layout.medication_alert_dialog, null)
        with(alertDialogBuilder) {
            setView(customLayout)

        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}