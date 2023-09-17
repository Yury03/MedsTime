package com.example.medstime.ui.add_med

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.Editable.Factory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.domain.models.MedicationModel
import com.example.medstime.R
import com.example.medstime.databinding.FragmentAddMedBinding
import com.example.medstime.ui.main_activity.MainActivity
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
import java.util.UUID


class AddMedFragment : Fragment(R.layout.fragment_add_med) {
    companion object Tag {
        const val LOG_TAG = "AddMedFragment"
        const val TIME_PICKER_TAG = "TimePicker"
    }

    private val viewModel by viewModel<AddMedViewModel>()
    private lateinit var binding: FragmentAddMedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMedBinding.inflate(inflater, container, false)
        hideBottomNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapterSpinDosageUnits()
        setAdapterSpinTrackType()
        setAdapterSpinReminderType()
        setAdapterSpinIntakeType()
        setAdapterSpinFrequency()
        setListenerForRemoveFocus()
        initView()
    }

    private fun setListenerForRemoveFocus() {
        //TODO добавить слушатель для удаления фокуса
    }


    private fun hideBottomNavigationBar() {
        (requireActivity() as MainActivity).hideBottomNavigationBar()
    }

    private fun showBottomNavigationBar() {
        (requireActivity() as MainActivity).showBottomNavigationBar()
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
                picker.show(parentFragmentManager, TIME_PICKER_TAG)
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
                medicationModel.first?.let {
                    Log.e(LOG_TAG, it.toString())
                    viewModel.saveNewMedication(it)
                    closeFragment()
                } ?: run {
                    showError(medicationModel.second)
                }
            }
            infoAboutBanner.setOnClickListener {
                showInfoAboutBannerDialog()
            }
            backButton.setOnClickListener {
                closeFragment()
            }
        }
    }

    private fun makeMedicationModel(): Pair<MedicationModel?, Int> {
        with(binding) {
            var errorCode = 0
            val medicationName = medicationName.text.toString()
            val medicationDosageStr = dosage.text.toString()
            val medicationDosageUnit = dosageUnits.text.toString()
            val medicationReminderTime = extractIntFromString(reminderType.text.toString())
            val medicationFrequency = getFrequency()
            val medicationSelectedDays =
                if (medicationFrequency.isDefault()) getSelectedDays() else null
            val medicationStartDate = startIntakeDate.text.toString().toDate()
            val medicationComment = medComment.text.toString()
            val medicationUseBanner = useBannerChBox.isChecked
            val medicationIntakeTimes = getIntakeTime()
            if (!trackingDataIsCorrect()) errorCode = 5
            if (!medicationFrequency.isCorrect()) errorCode = 4
            if (medicationIntakeTimes.isEmpty()) errorCode = 3
            if (medicationDosageStr.isEmpty()) errorCode = 2
            if (medicationName.isEmpty()) errorCode = 1
            if (errorCode == 0) {
                return Pair(
                    MedicationModel(
                        id = generateUniqueId(),
                        name = medicationName,
                        dosage = medicationDosageStr.toDouble(),
                        dosageUnit = medicationDosageUnit,
                        intakeTimes = medicationIntakeTimes,
                        reminderTime = medicationReminderTime,
                        frequency = medicationFrequency,
                        selectedDays = medicationSelectedDays,
                        startDate = medicationStartDate,
                        intakeType = getIntakeType(),
                        comment = medicationComment,
                        useBanner = medicationUseBanner,
                        trackType = getTrackingType(),
                        stockOfMedicine = getTrackingData().first,
                        numberOfDays = getTrackingData().second,
                        endDate = getTrackingData().third,
                    ), errorCode
                )
            } else {
                return Pair(null, errorCode)
            }
        }
    }

    private fun extractIntFromString(input: String): Int {
        val regex = Regex("\\d+")
        val matchResult = regex.find(input)
        return matchResult?.value?.toIntOrNull() ?: 0
    }

    private fun closeFragment() {
        requireActivity().findNavController(R.id.fragmentContainerView)
            .navigate(R.id.medicationFragment)
    }

    private fun showInfoAboutBannerDialog() =
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.dialog_title_info_about_banner)
            .setMessage(R.string.dialog_message_info_about_banner)
            .setPositiveButton(getString(R.string.positive_button_text)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()


    private fun showError(error: Int) {
        //todo добавить фронтенд: фокус на поле ввода, нужный цвет, подсказка
        Log.e(LOG_TAG, "ERROR CONTINUE BUTTON")
        val errorStr = when (error) {
            1 -> {
                getText(R.string.add_med_error_no_name).toString()
            }

            2 -> {
                getText(R.string.add_med_error_no_dosage).toString()
            }

            3 -> {
                getText(R.string.add_med_error_intake_time).toString()
            }

            4 -> {
                getText(R.string.add_med_error_selected_days).toString()

            }

            5 -> {
                getText(R.string.add_med_error_tracking).toString()
            }

            else -> getText(R.string.unknown_error).toString()

        }
        Toast.makeText(requireContext(), errorStr, Toast.LENGTH_SHORT).show()

    }


    private fun getFrequency(): MedicationModel.Frequency {
        val frequencyArray = resources.getStringArray(R.array.frequency_array)
        return when (binding.frequency.text.toString()) {
            frequencyArray[0] -> MedicationModel.Frequency.SELECTED_DAYS
            frequencyArray[1] -> MedicationModel.Frequency.EVERY_OTHER_DAY
            frequencyArray[2] -> MedicationModel.Frequency.DAILY
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

    private fun getIntakeTime(): List<MedicationModel.Time> {
        val intakeTimeArray = mutableListOf<MedicationModel.Time>()
        for (i in 0 until binding.chipGroupTime.childCount) {
            val chip = binding.chipGroupTime.getChildAt(i) as Chip
            intakeTimeArray.add(parseTimeString(chip.text.toString()))
        }
        return intakeTimeArray
    }

    private fun parseTimeString(chipText: String): MedicationModel.Time {
        val pair = chipText.split(':')
        return MedicationModel.Time(pair[0].toInt(), pair[1].toInt())
    }

    private fun getTrackingType(): MedicationModel.TrackType {
        val trackingTypeArray = resources.getStringArray(R.array.track_array)
        return when (binding.trackingType.text.toString()) {
            trackingTypeArray[0] -> MedicationModel.TrackType.NONE
            trackingTypeArray[1] -> MedicationModel.TrackType.STOCK_OF_MEDICINE
            trackingTypeArray[2] -> MedicationModel.TrackType.NUMBER_OF_DAYS
            trackingTypeArray[3] -> MedicationModel.TrackType.DATE
            else -> MedicationModel.TrackType.NONE
        }
    }


    private fun trackingDataIsCorrect(): Boolean {
        with(binding)
        {
            return when (getTrackingType()) {
                MedicationModel.TrackType.STOCK_OF_MEDICINE -> numberMeds.text.toString()
                    .isNotEmpty()

                MedicationModel.TrackType.DATE -> endIntakeDate.text.toString().isNotEmpty()
                MedicationModel.TrackType.NUMBER_OF_DAYS -> numberDays.text.toString().isNotEmpty()
                MedicationModel.TrackType.NONE -> true
            }
        }
    }

    /**На момент вызова getTrackingData() гарантируется, что соответствующее поле не пустое.
     *  Метод получает нужные данные, в зависимости от типа отслеживания*/
    private fun getTrackingData(): Triple<Double?, Double?, Date?> {
        with(binding) {
            val numberMedsStr = numberMeds.text.toString()
            val numberDaysStr = numberDays.text.toString()
            val endIntakeDateStr = endIntakeDate.text.toString()
            val trackArray = resources.getStringArray(R.array.track_array)
            return when (trackingType.text.toString()) {
                trackArray[0] -> Triple(null, null, null)
                trackArray[1] -> Triple(numberMedsStr.toDouble(), null, null)
                trackArray[2] -> Triple(null, numberDaysStr.toDouble(), null)
                trackArray[3] -> Triple(null, null, endIntakeDateStr.toDate())
                else -> Triple(null, null, null)
            }
        }
    }


    private fun getSelectedDays(): List<Int> {
        val selectedDays = mutableListOf<Int>()
        for (i in 0 until binding.chipGroupDaysWeek.childCount) {
            if ((binding.chipGroupDaysWeek.getChildAt(i) as Chip).isChecked) selectedDays.add(i + 1)
        }
        return selectedDays
    }


    private fun String.toDate(): Date {
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val localDate = LocalDate.parse(this, format)
        val instant = localDate.atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
        return Date.from(instant)
    }

    private fun MedicationModel.Frequency.isDefault(): Boolean {
        return this == MedicationModel.Frequency.SELECTED_DAYS
    }

    private fun MedicationModel.Frequency.isCorrect(): Boolean {
        return when (this) {
            MedicationModel.Frequency.DAILY -> true
            MedicationModel.Frequency.EVERY_OTHER_DAY -> true
            MedicationModel.Frequency.SELECTED_DAYS -> getSelectedDays().isNotEmpty()
        }
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
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
        val trackArray = resources.getStringArray(R.array.track_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, trackArray)
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

    override fun onDestroy() {
        super.onDestroy()
        showBottomNavigationBar()
    }
}
