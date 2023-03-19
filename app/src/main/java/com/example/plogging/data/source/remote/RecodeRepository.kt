package com.example.plogging.data.source.remote

import com.example.plogging.data.model.AloneRecode
import com.example.plogging.data.source.local.AloneRecodeDao
import kotlinx.coroutines.flow.Flow

class RecodeRepository(
    private val dao: AloneRecodeDao
) {
    fun getAllRecodes(): Flow<List<AloneRecode>> {
        return dao.getAllRecodes()
    }

    suspend fun insert(recode: AloneRecode): Long {
        return dao.insert(recode)
    }

    suspend fun delete(recode: AloneRecode) {
        dao.delete(recode)
    }

    fun getRecodesByDate(createDate: String): Flow<List<AloneRecode>> {
        return dao.getRecordsByDate(createDate)
    }
}