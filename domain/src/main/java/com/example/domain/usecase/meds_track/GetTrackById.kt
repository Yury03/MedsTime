package com.example.domain.usecase.meds_track

import com.example.domain.Repository
import com.example.domain.models.MedsTrackModel

class GetTrackById(private val repository: Repository.MedsTrackContract) {

    suspend operator fun invoke(medsTrackModelId: String): MedsTrackModel {
        return repository.getTrackById(medsTrackModelId)
    }
}