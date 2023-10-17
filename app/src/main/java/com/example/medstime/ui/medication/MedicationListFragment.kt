package com.example.medstime.ui.medication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.MedicationIntakeModel
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationListBinding
import com.example.medstime.ui.medication.adapters.TimesListAdapter
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicationListFragment : Fragment(R.layout.fragment_medication_list) {
    private val viewModel by viewModel<MedicationListViewModel>()
    private var _binding: FragmentMedicationListBinding? = null
    private val binding get() = _binding!!
    private lateinit var _date: MedicationIntakeModel.Date

    companion object {
        private const val TIME_PICKER_TAG = "TimePickerMedicationListFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicationListBinding.bind(view)
        val day = arguments?.getInt("day")!!                //todo обработка ошибок
        val month = arguments?.getInt("month")!!
        val year = arguments?.getInt("year")!!
        _date = (MedicationIntakeModel.Date(day, month, year))
        val medicationClick = { model: MedicationIntakeModel ->
            createItemButtonClickMap(model)
        }
        viewModel.initIntakeListToday()
        viewModel.intakeListToday.observe(viewLifecycleOwner) {
            val sortedList = splitIntakeList(it)
            if (sortedList.isEmpty()) {
                binding.placeholder.visibility = View.VISIBLE
                binding.medicationsList.visibility = View.GONE
            }

            binding.medicationsList.adapter = TimesListAdapter(
                dataList = sortedList,
                context = requireContext(),
                medicationClick = medicationClick,
            )
        }
        binding.medicationsList.layoutManager = LinearLayoutManager(context)
    }

    /**Метод splitIntakeList делит список приемов, на пары "время - список приемов" и сортирует возвращаемый список по времени*/
    private fun splitIntakeList(intakeList: List<MedicationIntakeModel>):
            List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>> {
        val list = intakeList.filter {
            it.intakeDate == _date
        }
        //если в данную дату есть приемы, то делим их на пары типа Время - Список приемов
        val groupedMedications: List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>> =
            list.groupBy { it.intakeTime }.map { (time, medications) ->
                time to medications
            }
        return groupedMedications.sortedWith(
            compareBy(
                { it.first.hour },
                { it.first.minute })
        )
    }

    private fun createItemButtonClickMap(intake: MedicationIntakeModel): Map<Int, View.OnClickListener> {
        val result = mutableMapOf<Int, View.OnClickListener>()
        with(result) {
            put(R.id.itemChangeTimeTakeButton) {
                val picker = callTimePicker(
                    intake.actualIntakeTime ?: intake.intakeTime
                )//todo  model.intakeTime: является ошибкой, защита от NPE, убрать
                picker.show(parentFragmentManager, TIME_PICKER_TAG)
                picker.addOnPositiveButtonClickListener {
                    viewModel.changeActualTime(
                        intake.id, MedicationIntakeModel.Time(picker.hour, picker.minute)
                    )
                }
            }
            put(R.id.itemTakenButton) {
                viewModel.changeIsTakenStatus(intake.id, true)
            }
            put(R.id.itemSkippedButton) {
                viewModel.changeIsTakenStatus(intake.id, false)
            }
            put(R.id.itemEditButton) {
                val args = Bundle()
                args.putString("mode", "EditMode")//TODO
                args.putString("medicationModelId", intake.medicationId)
                val navController = findNavController(requireActivity(), R.id.fragmentContainerView)
                navController.navigate(
                    resId = R.id.addMedFragment,
                    args = args,
                    navOptions = NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_right)
                        .setPopExitAnim(R.anim.slide_out_left)
                        .build()
                )
            }
            put(R.id.itemRemoveButton) {
                viewModel.removeMedicationModel(intake.medicationId)
            }
        }
        return result
    }

    private fun callTimePicker(oldTime: MedicationIntakeModel.Time) =
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(oldTime.hour)
            .setMinute(oldTime.minute).setTitleText(R.string.title_add_reminder)
            .setTheme(R.style.TimePickerDialog).build()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}