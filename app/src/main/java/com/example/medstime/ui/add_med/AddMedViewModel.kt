package com.example.medstime.ui.add_med

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationModel
import com.example.domain.usecase.addition.SaveNewMedication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddMedViewModel(
    private val saveNewMedication: SaveNewMedication,
) : ViewModel() {
    fun saveNewMedication(model: MedicationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            saveNewMedication.invoke(model)
        }
    }

}