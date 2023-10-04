package com.example.medstime.ui.add_med

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationModel
import com.example.domain.usecase.addition.SaveNewMedication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddMedViewModel(
    private val saveNewMedication: SaveNewMedication,
) : ViewModel() {
    private val _isSavedNewMedication: MutableLiveData<Boolean> =
        MutableLiveData()
    val isSavedNewMedication: LiveData<Boolean>
        get() = _isSavedNewMedication

    fun saveNewMedication(model: MedicationModel) {
        runBlocking {//TODO!!!
            viewModelScope.launch(Dispatchers.IO) {
                saveNewMedication.invoke(model)
                _isSavedNewMedication.postValue(true)
            }
        }
    }
}