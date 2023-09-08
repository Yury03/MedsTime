package com.example.medstime.ui.add_med

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable.Factory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.medstime.R
import com.example.medstime.databinding.FragmentAddMedBinding
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class AddMedFragment : DialogFragment() {
    private lateinit var binding: FragmentAddMedBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMedBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapterSpinDosageUnits()
        setAdapterSpinTrackType()
        setAdapterSpinReminderType()
        setAdapterSpinIntakeType()
        setAdapterSpinDurationSettings()
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        with(binding) {
            reminderLayoutButton.setOnClickListener {
                if ((reminderLayout.isExpanded)) reminderLayout.collapse()
                else reminderLayout.expand()
            }
            durationLayoutButton.setOnClickListener {
                if (durationLayout.isExpanded) durationLayout.collapse()
                else durationLayout.expand()
            }
            addIntakeTime.setOnClickListener {
                val picker = callTimePicker(R.string.title_add_reminder)
                picker.addOnPositiveButtonClickListener {
                    if (picker.minute < 10)
                        addChipTime("${picker.hour}:0${picker.minute}")
                    else
                        addChipTime("${picker.hour}:${picker.minute}")
                }
                picker.show(parentFragmentManager, "TimePicker")
            }

            startIntakeDate.text = Factory.getInstance().newEditable(getCurrentDate())
            startIntakeDate.setOnClickListener {
                showDatePickerDialog(startIntakeDate)
            }
            endIntakeDate.setOnClickListener {
                showDatePickerDialog(endIntakeDate)
            }
        }
    }


    private fun showDatePickerDialog(textView: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    Locale.getDefault(),
                    "%02d.%02d.%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )
                textView.text = Factory.getInstance().newEditable(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return LocalDate.now().format(formatter)
    }

    private fun addChipTime(chipText: String) {
        val newChip = Chip(binding.chipGroupTime.context)
        newChip.apply {
            text = chipText
            setStyle(
                com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox_ExposedDropdownMenu,
                theme
            )
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                binding.chipGroupTime.removeView(newChip)
            }
        }
        binding.chipGroupTime.addView(newChip, binding.chipGroupTime.childCount)
    }

    private fun callTimePicker(textId: Int) =
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText(textId)
            .build()


    private fun setAdapterSpinDosageUnits() {
        val dosageArray = resources.getStringArray(R.array.dosage_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, dosageArray)
        (binding.textFieldUnits.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setAdapterSpinTrackType() {
        val typeArray = resources.getStringArray(R.array.track_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, typeArray)
        (binding.textFieldTrackingType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        with(binding) {
            trackingType.setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {//интерфейс "Не добавлять"
                        textFieldEndIntakeDate.visibility = View.GONE
                        textFieldNumberDays.visibility = View.GONE
                        textFieldNumberMeds.visibility = View.GONE
                    }

                    1 -> {//интерфейс "Запас лекарств"
                        textFieldNumberDays.visibility = View.GONE
                        textFieldEndIntakeDate.visibility = View.GONE
                        textFieldNumberMeds.visibility = View.VISIBLE
                    }

                    2 -> {//интерфейс "Количество дней"
                        textFieldNumberDays.visibility = View.VISIBLE
                        textFieldEndIntakeDate.visibility = View.GONE
                        textFieldNumberMeds.visibility = View.GONE
                    }

                    3 -> {//интерфейс "Дата"
                        textFieldEndIntakeDate.visibility = View.VISIBLE
                        textFieldNumberDays.visibility = View.GONE
                        textFieldNumberMeds.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun setAdapterSpinReminderType() {
        val reminderArray = resources.getStringArray(R.array.reminder_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, reminderArray)
        (binding.textFieldReminderType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setAdapterSpinIntakeType() {
        val intakeArray = resources.getStringArray(R.array.intake_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, intakeArray)
        (binding.textFieldIntakeType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setAdapterSpinDurationSettings() {
        val durationSettingsArray = resources.getStringArray(R.array.duration_settings_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, durationSettingsArray)
        (binding.textFieldDurationSettings.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        with(binding) {
            durationSettings.setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {//интерфейс "выбранные дни"
                        chipGroupDaysWeek.visibility = View.VISIBLE
                    }

                    1 -> {//интерфейс "Каждый день"
                        chipGroupDaysWeek.visibility = View.GONE
                    }

                    2 -> {//интерфейс "Через день"
                        chipGroupDaysWeek.visibility = View.GONE
                    }
                }
            }
        }
    }

}
