package com.example.medstime.ui.medication

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationBinding
import com.example.medstime.domain.models.MedicationIntakeModel
import com.example.medstime.ui.medication.adapters.TimesListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class MedicationFragment : Fragment(R.layout.fragment_medication) {

    private var _binding: FragmentMedicationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MedicationViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
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
                    MedicationIntakeModel.Date(
                        dayOfMonth,
                        month + 1
                    )
                )//todo
                hideCalendar.callOnClick()
            }
            val maxDate = (1000 * 3600 * 24 * 14)
            val minDate = (1000 * 3600 * 24 * 14)
            calendar.minDate = (Date().time - minDate)
            calendar.maxDate = (Date().time + maxDate)
        }

        val medicationClick = { it: MedicationIntakeModel ->
            showAlertDialog(it)
        }
        viewModel.intakeListToday.observe(viewLifecycleOwner) {
            binding.medicationsList.adapter =
                context?.let { it1 -> TimesListAdapter(it, it1, medicationClick) }
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
    private fun showAlertDialog(medicationIntakeModel: MedicationIntakeModel) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        val customLayout: View = layoutInflater.inflate(R.layout.medication_alert_dialog, null)
        with(alertDialogBuilder) {
            setView(customLayout)
            background = ColorDrawable(Color.TRANSPARENT)
        }
        val alertDialog = alertDialogBuilder.create()
        with(customLayout) {
            findViewById<TextView>(R.id.AD_medicationName).text = medicationIntakeModel.name
            findViewById<TextView>(R.id.AD_timeAndDosage).text =
                buildTimeAndDosageText(medicationIntakeModel)
            findViewById<ImageButton>(R.id.AD_editButton).setOnClickListener {
            }
            findViewById<AppCompatButton>(R.id.AD_remindFiveMinButton).setOnClickListener {
                alertDialog.dismiss()
            }
            findViewById<AppCompatButton>(R.id.AD_skipButton).setOnClickListener {
                alertDialog.dismiss()
            }
            findViewById<AppCompatButton>(R.id.AD_takenButton).setOnClickListener {
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    private fun buildTimeAndDosageText(medicationIntakeModel: MedicationIntakeModel): String {
        val time = buildTimeString(medicationIntakeModel.intakeTime)
        val dosage = buildDosageString(medicationIntakeModel)
        return "$time $dosage"
    }

    private fun buildTimeString(time: MedicationIntakeModel.Time): String =
        with(time) {
            if (minute < 10) return "$hour:0$minute"
            else return "$hour:$minute"
        }

    private fun buildDosageString(medicationIntakeModel: MedicationIntakeModel): String {
        val commonText =
            "${medicationIntakeModel.dosageUnit} ${medicationIntakeModel.intakeType.toRussianString()}"
        return if (medicationIntakeModel.dosage == medicationIntakeModel.dosage.toInt().toDouble())
            "${medicationIntakeModel.dosage.toInt()} $commonText"
        else
            "${medicationIntakeModel.dosage} $commonText"
    }

    private fun MedicationIntakeModel.IntakeType.toRussianString() =
        when (this) {
            MedicationIntakeModel.IntakeType.AFTER_MEAL -> getString(R.string.after_meal)
            MedicationIntakeModel.IntakeType.BEFORE_MEAL -> getString(R.string.before_meal)
            MedicationIntakeModel.IntakeType.DURING_MEAL -> getString(R.string.during_meal)
            MedicationIntakeModel.IntakeType.NONE -> getString(R.string.empty_string)
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}