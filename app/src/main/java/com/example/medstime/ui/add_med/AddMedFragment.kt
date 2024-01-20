package com.example.medstime.ui.add_med

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable.Factory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.domain.models.MedicationModel
import com.example.medstime.R
import com.example.medstime.databinding.FragmentAddMedBinding
import com.example.medstime.services.ReminderService
import com.example.medstime.ui.main_activity.MainActivity
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.Locale


class AddMedFragment : Fragment(R.layout.fragment_add_med) {

    private val viewModel by viewModel<AddMedViewModel>()
    private var _binding: FragmentAddMedBinding? = null
    private val binding get() = _binding!!
    private lateinit var _currentState: AddMedState//todo слишком много полей, на каждое нужен event?

    //private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext()) //ошибка с библиотекой камеры? Появилась после изменения грэдл. Приложение крашится
        _binding = FragmentAddMedBinding.inflate(inflater, container, false)
        hideBottomNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelData()
        handleArguments()
        setAdapterSpinDosageUnits()
        setAdapterSpinTrackType()
        setAdapterSpinReminderType()
        setAdapterSpinIntakeType()
        setAdapterSpinFrequency()
//        betaFunctions()
        initView()
    }

    private fun hideBottomNavigationBar() {
        (requireActivity() as MainActivity).hideBottomNavigationBar()
    }

    private fun showBottomNavigationBar() {
        (requireActivity() as MainActivity).showBottomNavigationBar()
    }

    private fun handleArguments() {
        arguments?.let { args ->
            if (args.getString(ARG_KEY_MODE).equals(AddMedState.EDIT_MODE)) {
                val id = args.getString(ARG_KEY_MEDICATION_MODEL_ID)!!
                viewModel.send(AddMedEvent.Mode(AddMedState.EDIT_MODE, id))
                changeViewTextToEditMode()
            }
            //передача текущего состояния фрагмента, для последующего восстановления
            val stateJson = args.getString(ARG_KEY_STATE)
            stateJson?.let {
                val addMedState = Gson().fromJson(it, AddMedState::class.java)
                updateAllData(state = addMedState, isForcedUpdate = true)
                viewModel.send(AddMedEvent.UpdateState(addMedState))
            }
        }
    }

    private fun changeViewTextToEditMode() {
        with(binding) {
            title.setText(R.string.edit_med)
            reminderLayoutButton.setText(R.string.edit_reminder)
            addMedButton.setText(R.string.edit_med)
        }
    }

    private fun medicationSaved() {
        val serviceIntent = Intent(requireContext(), ReminderService::class.java)
        requireContext().startService(serviceIntent)

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
                val picker = callTimePicker()
                picker.addOnPositiveButtonClickListener {
                    addChipTime(picker.hour, picker.minute)
                }
                picker.show(parentFragmentManager, TIME_PICKER_TAG)
            }
            startIntakeDate.setOnClickListener {
                showDatePickerDialog(startIntakeDate)
            }
            endIntakeDate.setOnClickListener {
                showDatePickerDialog(endIntakeDate)
            }
            addMedButton.setOnClickListener {
                with(viewModel) {
                    updateCurrentState()
                    send(AddMedEvent.UpdateState(_currentState))
                    send(AddMedEvent.ContinueButtonClicked)
                }
            }
            backButton.setOnClickListener {
                closeFragment(DESTINATION_TO_MEDICATION_SCREEN)
            }
//            scanBarcode.setOnClickListener {
//                checkCameraPermission()
//                if (scanBarcodeLayout.isExpanded) scanBarcodeLayout.collapse()
//                else scanBarcodeLayout.expand()
//            }
            medicationName.addTextChangedListener {
                if (textFieldMedicationName.isErrorEnabled) {
                    textFieldMedicationName.isErrorEnabled = false
                }
            }
            dosage.addTextChangedListener {
                if (textFieldDosage.isErrorEnabled) {
                    textFieldDosage.isErrorEnabled = false
                }
            }
            buttonAddMedTrack.setOnClickListener {
                openAddMedTrackFragment()
            }
        }
    }

    private fun observeViewModelData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    _currentState = state
                    Log.e(LOG_TAG, state.toString())
                    updateAllData(state)
                }
            }
        }
    }

    /**Функция updateAllData() обновляет все данные в случае редактирования модели или isForcedUpdate == true,
     * в остальных случаях обновляется только дата начала приема, код ошибки и факт сохранения новой модели. */
    private fun updateAllData(state: AddMedState, isForcedUpdate: Boolean = false) {
        with(binding) {
            startIntakeDate.updateText(state.startIntakeDate)
            if (state.mode == AddMedState.EDIT_MODE || isForcedUpdate) {
                medicationName.updateText(state.medicationName)
                dosage.updateText(state.dosage)
                dosageUnits.updateAutoCompleteText(state.dosageUnits)
                medComment.updateText(state.medComment)
                useBannerChBox.updateIsChecked(state.useBannerChBox)
                updateIntakeTimeChips(state.intakeTimeList)
                reminderType.updateAutoCompleteText(state.medicationReminderTime)
                intakeType.updateAutoCompleteText(state.intakeType)
                frequency.updateAutoCompleteText(state.frequency)
                updateSelectedDays(state.selectedDays)
                trackingType.updateAutoCompleteText(state.trackType)
                numberDays.updateText(state.numberOfDays.toString())
                endIntakeDate.updateText(state.endDate)
            }
        }
        if (state.inputError != 0) {
            showError(state.inputError)
            viewModel.send(AddMedEvent.ErrorWasShown)
        }
        if (state.isSavedNewMedication) {
            medicationSaved()
            closeFragment(DESTINATION_TO_MEDICATION_SCREEN)
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

    private fun getSelectedDays(): List<Int> {
        val selectedDays = mutableListOf<Int>()
        for (i in 0 until binding.chipGroupDaysWeek.childCount) {
            if ((binding.chipGroupDaysWeek.getChildAt(i) as Chip).isChecked) selectedDays.add(i + 1)
        }
        return selectedDays
    }

//    private fun checkSystemAlertWindowPermission(): Boolean {
//        val systemAlertWindowIsGranted = ContextCompat.checkSelfPermission(
//            requireContext(), Manifest.permission.SYSTEM_ALERT_WINDOW
//        ) == PackageManager.PERMISSION_DENIED
//        if (systemAlertWindowIsGranted) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW),
//                SYSTEM_ALERT_WINDOW_CODE
//            )
//        }
//        if (!Settings.canDrawOverlays(requireContext())) {
//            val settingsIntent = Intent(
//                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                Uri.parse("package:${requireContext().packageName}")
//            )
//            //TODO:3 Dialog
//            startActivity(settingsIntent)
//        }
//        return Settings.canDrawOverlays(requireContext()) && systemAlertWindowIsGranted
//    }

    /**## Метод closeFragment() обрабатывает все операции перехода с помощью **navController**.*/
    private fun closeFragment(destination: Int, args: Bundle = Bundle()) {
        when (destination) {
            DESTINATION_TO_MEDICATION_SCREEN -> {
                showBottomNavigationBar()
                requireActivity().findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.medicationFragment)
            }

            DESTINATION_TO_ADD_MED_TRACK_SCREEN -> {
                requireActivity().findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.addMedTrackFragment, args)
            }
        }
    }

    private fun openAddMedTrackFragment() {
        updateCurrentState()
        val stateJson = Gson().toJson(_currentState)
        val args = Bundle().apply {
            putString(ARG_KEY_STATE, stateJson)
        }
        closeFragment(DESTINATION_TO_ADD_MED_TRACK_SCREEN, args)
    }

//    private fun showInfoAboutBannerDialog() =
//        AlertDialog.Builder(requireActivity()).setTitle(R.string.dialog_title_info_about_banner)
//            .setMessage(R.string.dialog_message_info_about_banner)
//            .setPositiveButton(getString(R.string.positive_button_text)) { dialog, _ ->
//                dialog.dismiss()
//            }.create().show()

    private fun showError(error: Int) {
        Log.e(LOG_TAG, "ERROR CONTINUE BUTTON")
        val errorStr = when (error) {
            1 -> {
                binding.textFieldMedicationName.error = getText(R.string.required_field)
                binding.textFieldMedicationName.requestFocus()
                getText(R.string.add_med_error_no_name).toString()
            }

            2 -> {
                binding.textFieldDosage.error = getText(R.string.required_field)
                binding.textFieldDosage.requestFocus()
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

    private fun parseTimeString(chipText: String): MedicationModel.Time {
        val pair = chipText.split(':')
        return MedicationModel.Time(pair[0].toInt(), pair[1].toInt())
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

    private fun callTimePicker(hour: Int = 12, minute: Int = 0) =
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(hour)
            .setMinute(minute).setTitleText(R.string.title_add_reminder)
            .setTheme(R.style.TimePickerDialog).build()

    private fun addChipTime(hour: Int, minute: Int) {
        val chipText = timeToDisplayString(hour, minute)
        val timeChips = getIntakeTime()
        val chipTime = parseTimeString(chipText)
        timeChips.forEach {
            if (it.hour == chipTime.hour && it.minute == parseTimeString(chipText).minute) return //проверка на совпадение с уже существующим чипом
        }
        val newChip = Chip(binding.chipGroupTime.context)
        newChip.apply {
            text = chipText
            setTextColor(ContextCompat.getColorStateList(context, R.color.black))
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                binding.chipGroupTime.removeView(newChip)
            }
            chipBackgroundColor =
                ContextCompat.getColorStateList(context, R.color.meds_time_primary)
            closeIconTint = ContextCompat.getColorStateList(context, R.color.chip_close_icon_tint)
            setOnClickListener {
                val picker = callTimePicker(hour, minute)
                picker.addOnPositiveButtonClickListener {
                    val editChipText = timeToDisplayString(picker.hour, picker.minute)
                    this.text = editChipText
                }
                picker.show(parentFragmentManager, TIME_PICKER_TAG)
            }
        }
        binding.chipGroupTime.addView(newChip, binding.chipGroupTime.childCount)
    }

    private fun setAdapterSpinDosageUnits() {
        val dosageArray = resources.getStringArray(R.array.dosage_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, dosageArray)
        (binding.textFieldUnits.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setAdapterSpinTrackType() {
        val trackArray = resources.getStringArray(R.array.track_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_item, trackArray)
        (binding.textFieldTrackingType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.trackingType.addTextChangedListener {
            it?.let { text ->
                updateViewVisibility(text.toString())
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
        binding.frequency.addTextChangedListener {
            it?.let { text ->
                updateViewVisibility(text.toString())
            }
        }
    }

//    private fun betaFunctions() {
//        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
//        val defaultValue = resources.getBoolean(R.bool.sp_camera_beta_default)
//        val cameraBeta = sharedPref.getBoolean(getString(R.string.sp_key_camera_beta), defaultValue)
//        if (cameraBeta) {
//            activateCamera()
//            activateUseBanner()
//            binding.scanBarcode.visibility = View.VISIBLE
//        }
//    }

//    /**Использование баннера работает неправильно, периодически крашится*/
//    private fun activateUseBanner() {
//        with(binding) {
//            bannerLayout.visibility = View.VISIBLE
//            useBannerChBox.setOnClickListener {
//                if (!checkSystemAlertWindowPermission()) {
//                    (it as CheckBox).isChecked = false
//                }
//            }
//            infoAboutBanner.setOnClickListener {
//                showInfoAboutBannerDialog()
//            }
//        }
//    }

//    private fun checkCameraPermission() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_DENIED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
//            )
//        }
//    }

//    private fun activateCamera() {
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//            bindPreview(cameraProvider)
//        }, ContextCompat.getMainExecutor(requireContext()))
//    }

//    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
//        val preview: Preview = Preview.Builder().build()
//        val cameraSelector: CameraSelector =
//            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
//        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
//        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
//    }

    private fun AutoCompleteTextView.updateAutoCompleteText(text: String) {
        if (this.text.toString() != text) {
            setText(text, false)
            updateViewVisibility(text)
        }
    }

    private fun updateViewVisibility(text: String) {
        val trackArray = resources.getStringArray(R.array.track_array)
        val frequencyArray = resources.getStringArray(R.array.frequency_array)
        with(binding) {
            when (text) {
                trackArray[0] -> {//интерфейс "Не добавлять"
                    textFieldEndIntakeDate.visibility = View.GONE
                    textFieldNumberDays.visibility = View.GONE
                    textFieldNumberMeds.visibility = View.GONE
                    buttonAddMedTrack.visibility = View.GONE
                }

                trackArray[1] -> {//интерфейс "Курс (количество лекарств)"
                    textFieldEndIntakeDate.visibility = View.GONE
                    textFieldNumberDays.visibility = View.GONE
                    textFieldNumberMeds.visibility = View.VISIBLE
                    buttonAddMedTrack.visibility = View.GONE
                }

                trackArray[2] -> {//интерфейс "Курс (количество дней)"
                    textFieldEndIntakeDate.visibility = View.GONE
                    textFieldNumberDays.visibility = View.VISIBLE
                    textFieldNumberMeds.visibility = View.GONE
                    buttonAddMedTrack.visibility = View.GONE
                }

                trackArray[3] -> {//интерфейс "Дата"
                    textFieldEndIntakeDate.visibility = View.VISIBLE
                    textFieldNumberDays.visibility = View.GONE
                    textFieldNumberMeds.visibility = View.GONE
                    buttonAddMedTrack.visibility = View.GONE
                }

                trackArray[4] -> {//интерфейс "Запас лекарств"
                    textFieldEndIntakeDate.visibility = View.GONE
                    textFieldNumberDays.visibility = View.GONE
                    textFieldNumberMeds.visibility = View.GONE
                    buttonAddMedTrack.visibility = View.VISIBLE
                }

                frequencyArray[0] -> {//интерфейс "выбранные дни"
                    chipGroupDaysWeek.visibility = View.VISIBLE
                }

                frequencyArray[1], frequencyArray[2] -> {//интерфейс "Каждый день", "Через день"
                    chipGroupDaysWeek.visibility = View.GONE
                }
            }
        }
    }

    private fun updateSelectedDays(selectedDays: List<Int>) {
        for (i in selectedDays) {
            val chip = binding.chipGroupDaysWeek.getChildAt(i - 1) as Chip
            if (!chip.isChecked) {
                chip.isChecked = true
            }
        }
    }

    private fun updateIntakeTimeChips(intakeTime: List<MedicationModel.Time>) {
        intakeTime.forEach {
            addChipTime(it.hour, it.minute)
        }
    }

    private fun CheckBox.updateIsChecked(isChecked: Boolean) {
        if (this.isChecked != isChecked) {
            this.isChecked = isChecked
        }
    }

    private fun TextView.updateText(text: String) {
        if (this.text.toString() != text) {
            this.text = text
        }
    }

    private fun timeToDisplayString(hour: Int, minute: Int) = if (minute < 10) "${hour}:0${minute}"
    else "$hour:$minute"

    private fun updateCurrentState() {
        val stockOfMedicine =
            binding.numberMeds.text.toString().takeIf { it.isNotBlank() }?.toDouble()
        val numberOfDays = binding.numberDays.text.toString().takeIf { it.isNotBlank() }?.toInt()
        Log.e(LOG_TAG, _currentState.toString())
        _currentState = _currentState.copy(
            medicationName = binding.medicationName.text.toString(),
            dosage = binding.dosage.text.toString(),
            dosageUnits = binding.dosageUnits.text.toString(),
            startIntakeDate = binding.startIntakeDate.text.toString(),
            medComment = binding.medComment.text.toString(),
            useBannerChBox = binding.useBannerChBox.isChecked,
            intakeTimeList = getIntakeTime(),
            medicationReminderTime = binding.reminderType.text.toString(),
            frequency = binding.frequency.text.toString(),
            selectedDays = getSelectedDays(),
            trackType = binding.trackingType.text.toString(),
            stockOfMedicine = stockOfMedicine ?: 0.0, //todo test
            numberOfDays = numberOfDays ?: 0, //todo test
            endDate = binding.endIntakeDate.text.toString(),
        )
    }

    companion object {

        private const val LOG_TAG = "AddMedFragment"
        private const val TIME_PICKER_TAG = "TimePickerAddMedFragment"
        private const val DESTINATION_TO_MEDICATION_SCREEN = 1
        private const val DESTINATION_TO_ADD_MED_TRACK_SCREEN = 2
        const val ARG_KEY_STATE = "state"
        const val ARG_KEY_MODE = "mode"
        const val ARG_KEY_MEDICATION_MODEL_ID = "medication_model_id"
        //private const val CAMERA_PERMISSION_CODE = 300
        //private const val SYSTEM_ALERT_WINDOW_CODE = 400
    }
}