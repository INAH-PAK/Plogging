package com.example.plogging.data.source.local

import androidx.room.*
import com.example.plogging.data.model.AloneRecode
import kotlinx.coroutines.flow.Flow

@Dao
interface AloneRecodeDao {

    @Insert
    suspend fun insert(recode: AloneRecode) : Long

    @Query("SELECT * FROM local_recodes")
     fun getAllRecodes(): Flow<List<AloneRecode>>

    @Delete
     suspend fun delete(aloneRecode: AloneRecode)

    @Query("SELECT * FROM local_recodes WHERE create_date LIKE :createDate")
     fun getRecordsByDate(createDate:String) : Flow<List<AloneRecode>>
}
