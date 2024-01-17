package com.example.medstime.ui.meds_tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.meds_track.GetAllTracks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedsTrackViewModel(
    private val getAllTracksUseCase: GetAllTracks,
) : ViewModel() {

    private val _state: MutableLiveData<MedsTrackState> =
        MutableLiveData()
    val state: LiveData<MedsTrackState>
        get() = _state

    init {
        _state.value = MedsTrackState(isLoading = true, medsTrackList = listOf())
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(
                MedsTrackState(
                    isLoading = false,
                    medsTrackList = getAllTracksUseCase.invoke()
                )
            )
        }
    }
}
