package com.example.data

import android.content.Context
import com.example.data.mappers.MedsTrackMapper
import com.example.data.room.MedsTrackDatabase
import com.example.data.room.dao.MedsTrackDao
import com.example.domain.Repository
import com.example.domain.models.MedsTrackModel

class MedsTrackContractImpl(context: Context) : Repository.MedsTrackContract {
    private val medsTrackDatabase: MedsTrackDatabase by lazy {
        MedsTrackDatabase.getDatabase(context)
    }
    private val medsTrackDao: MedsTrackDao by lazy { medsTrackDatabase.medsTrackDao() }
    override suspend fun getAllTracks(): List<MedsTrackModel> {
        return medsTrackDao.getAll().map {
            MedsTrackMapper.mapToModel(it)
        }
    }
}