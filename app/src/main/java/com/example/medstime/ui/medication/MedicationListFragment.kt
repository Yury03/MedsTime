package com.example.medstime.ui.medication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.MedicationIntakeModel
import com.example.medstime.R
import com.example.medstime.databinding.FragmentMedicationListBinding
import com.example.medstime.ui.medication.adapters.TimesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


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
                                timeAndDosageText: String, itemHolder: View ->
            createItemButtonClickMap(model)

        }

        viewModel.intakeListToday.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.placeholder.visibility = View.GONE
                binding.medicationsList.visibility = View.VISIBLE
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

    private fun createItemButtonClickMap(model: MedicationIntakeModel): Map<Int, View.OnClickListener> {
        val result = mutableMapOf<Int, View.OnClickListener>()
        with(result) {
            put(R.id.itemChangeTimeTakeButton) {

            }
            put(R.id.itemTakenButton) {
                viewModel.changeIsTakenStatus(model.id, true)
            }
            put(R.id.itemSkippedButton) {
                viewModel.changeIsTakenStatus(model.id, false)
            }
            put(R.id.itemEditButton) {

            }
            put(R.id.itemRemoveButton) {

            }
        }
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}