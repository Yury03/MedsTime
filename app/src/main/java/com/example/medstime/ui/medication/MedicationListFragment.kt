package com.example.medstime.ui.medication

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.MedicationIntakeModel
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationListBinding
import com.example.medstime.receivers.MedicationReminderReceiver
import com.example.medstime.ui.medication.adapters.TimesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar


class MedicationListFragment : Fragment(R.layout.fragment_medication_list) {
    private val viewModel by viewModel<MedicationListViewModel>()
    private var _binding: FragmentMedicationListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicationListBinding.bind(view)
        val day = arguments?.getInt("day")!!                //todo обработка ошибок
        val month = arguments?.getInt("month")!!
        val year = arguments?.getInt("year")!!
        val medicationClick = { model: MedicationIntakeModel,
                                timeAndDosageText: String ->
//            MedicationClickAlert(requireContext(), model, timeAndDosageText).show()
        }
        viewModel.intakeListToday.observe(viewLifecycleOwner) {
            it.map {
                it.second.map {
                    if (it.intakeDate.day == 25) {//todo
                        generateBanners(intakeModel = it)
                    }
                }
            }
            binding.medicationsList.adapter = TimesListAdapter(
                dataList = it,
                context = requireContext(),
                medicationClick = medicationClick,
            )
        }
        binding.medicationsList.layoutManager = LinearLayoutManager(context)
        viewModel.getIntakeListWithDate(MedicationIntakeModel.Date(day, month, year))
    }

    private fun generateBanners(intakeModel: MedicationIntakeModel) {
        val alarmManager = ContextCompat.getSystemService(
            requireContext(),
            AlarmManager::class.java
        ) as AlarmManager
//        for (intakeModel in intakeModels) {
        val intent = Intent(context, MedicationReminderReceiver::class.java)
        intent.putExtra("medicationModelID", intakeModel.id)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        val intakeTime = intakeModel.intakeTime
        val intakeDate = intakeModel.intakeDate
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, intakeDate.year)
        calendar.set(Calendar.MONTH, intakeDate.month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, intakeDate.day)
        calendar.set(Calendar.HOUR_OF_DAY, intakeTime.hour)
        calendar.set(Calendar.MINUTE, intakeTime.minute)
        calendar.set(Calendar.SECOND, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)//todo
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}