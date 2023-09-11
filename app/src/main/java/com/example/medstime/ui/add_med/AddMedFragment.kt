package com.example.medstime.ui.add_med

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.Editable.Factory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.example.domain.models.MedicationModel
import com.example.medstime.R
import com.example.medstime.databinding.FragmentAddMedBinding
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddMedFragment : DialogFragment() {
    companion object Tag {
        const val TAG: String = "AddMedFragment"
    }

    private val viewModel by viewModel<AddMedViewModel>()
    private lateinit var binding: FragmentAddMedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMedBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapterSpinDosageUnits()
        setAdapterSpinTrackType()
        setAdapterSpinReminderType()
        setAdapterSpinIntakeType()
        setAdapterSpinFrequency()
        initView()
    }

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
                    if (picker.minute < 10) addChipTime("${picker.hour}:0${picker.minute}")
                    else addChipTime("${picker.hour}:${picker.minute}")
                }
                picker.show(parentFragmentManager, "TimePicker")
            }
            startIntakeDate.text = getCurrentDate()
            startIntakeDate.setOnClickListener {
                showDatePickerDialog(startIntakeDate)
            }
            endIntakeDate.setOnClickListener {
                showDatePickerDialog(endIntakeDate)
            }
            continueButton.setOnClickListener {
                val medicationModel = makeMedicationModel()
                medicationModel?.let {
                    Log.e(TAG, it.toString())
                } ?: run {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Log.e(TAG, "ERROR CONTINUE BUTTON")
    }

    private fun getFrequency(): MedicationModel.Frequency {
        val frequencyArray = resources.getStringArray(R.array.frequency_array)
        return when (binding.frequency.text.toString()) {
            frequencyArray[0] -> MedicationModel.Frequency.SELECTED_DAYS
            frequencyArray[1] -> MedicationModel.Frequency.DAILY
            frequencyArray[2] -> MedicationModel.Frequency.EVERY_OTHER_DAY
            else -> MedicationModel.Frequency.SELECTED_DAYS
        }
    }

    private fun getIntakeType(): MedicationModel.IntakeType {
        val intakeTypeArray = resources.getStringArray(R.array.intake_array)
        return when (binding.intakeType.text.toString()) {
            intakeTypeArray[0] -> MedicationModel.IntakeType.NONE
            intakeTypeArray[1] -> MedicationModel.IntakeType.BEFORE_MEAL
            intakeTypeArray[2] -> MedicationModel.IntakeType.DURING_MEAL
            intakeTypeArray[3] -> MedicationModel.IntakeType.AFTER_MEAL
            else -> MedicationModel.IntakeType.NONE
        }
    }

    private fun getIntakeTime(): List<MedicationModel.Time> {//todo оптимизировать
        val intakeTimeArray = mutableListOf<MedicationModel.Time>()
        for (i in 0 until binding.chipGroupTime.childCount) {
            val chip = binding.chipGroupTime.getChildAt(i) as Chip
            val chipText = chip.text.toString()
            val pair = chipText.split(':')
            intakeTimeArray.add(i, MedicationModel.Time(pair[0].toInt(), pair[1].toInt()))
        }
        return intakeTimeArray
    }

    private fun makeMedicationModel(): MedicationModel? {
        with(binding) {
            val medicationName = medicationName.text.toString()
            val medicationDosageStr = dosage.text.toString()
            val medicationDosageUnit = dosageUnits.text.toString()
            val medicationIntakeTime = getIntakeTime()
            val medicationReminderTime = extractIntFromString(reminderType.text.toString())
            val medicationFrequency = getFrequency()
            val medicationSelectedDays =
                if (medicationFrequency.isDefault()) getSelectedDays() else null
            val medicationStartDate = startIntakeDate.text.toString().toDate()
            val medicationEndDate =
                if (!medicationFrequency.isDefault()) {
                    endIntakeDate.text.toString()
                        .takeIf { it.isNotEmpty() }?.toDate()

                } else null
            if (medicationName.isNotEmpty() && medicationDosageStr.isNotEmpty()) {


                return MedicationModel(
                    id = generateUniqueId(),
                    name = medicationName,
                    dosage = medicationDosageStr.toDouble(),
                    dosageUnit = medicationDosageUnit,
                    intakeTimes = medicationIntakeTime,
                    reminderTime = medicationReminderTime,
                    frequency = medicationFrequency,
                    selectedDays = medicationSelectedDays,
                    startDate = medicationStartDate,
                    endDate = medicationEndDate,
                    intakeType = getIntakeType(),
                )
            } else {
                return null
            }
        }
    }

    private fun getSelectedDays(): List<Int> {
        val selectedDays = mutableListOf<Int>()
        for (i in 0 until binding.chipGroupDaysWeek.childCount) {
            if (binding.chipGroupDaysWeek[i].isSelected) selectedDays.add(i + 1)
        }
        return selectedDays
    }

    private fun extractIntFromString(input: String): Int {
        val regex = Regex("\\d+")
        val matchResult = regex.find(input)
        return matchResult?.value?.toIntOrNull() ?: 0
    }


    @SuppressLint("SimpleDateFormat")//todo
    private fun String.toDate(): Date {
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.parse(this)
    }

    private fun MedicationModel.Frequency.isDefault(): Boolean {
        return this == MedicationModel.Frequency.SELECTED_DAYS
    }

    private fun generateUniqueId(): String {
        val timestamp = System.currentTimeMillis()
        val random = (Math.random() * 1000).toInt()
        return "$timestamp$random"
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
            }, year, month, day
        )
        datePickerDialog.show()
    }


    private fun getCurrentDate(): Editable {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return Factory.getInstance().newEditable(LocalDate.now().format(formatter))
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
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12).setMinute(0)
            .setTitleText(textId).build()

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

    private fun setAdapterSpinFrequency() {
        val frequencyArray = resources.getStringArray(R.array.frequency_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, frequencyArray)
        (binding.textFieldFrequency.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        with(binding) {
            frequency.setOnItemClickListener { _, _, position, _ ->
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
