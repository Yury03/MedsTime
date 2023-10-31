package com.example.medstime.ui.meds_tracking

import com.example.domain.models.MedsTrackModel

data class MedsTrackState(
    val isLoading: Boolean,
    val medsTrackList: List<MedsTrackModel>,
)
