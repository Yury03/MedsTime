package com.example.domain.usecase.meds_track

import com.example.domain.Repository
import com.example.domain.models.MedsTrackModel

class GetAllTracks(private val repository: Repository.MedsTrackContract) {

    suspend operator fun invoke(): List<MedsTrackModel> {
        return repository.getAllTracks()
    }
}