package com.gjapps.minilogbook.data.datasources.storage.local.room.daos

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Entity(tableName = "BloodGlucoseRecordEntity")
data class BloodGlucoseRecordEntity(
    val value: Float,val date: Date
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface BloodGlucoseRecordDao {
    @Query("SELECT * FROM BloodGlucoseRecordEntity ORDER BY uid DESC")
    fun getAll(): Flow<List<BloodGlucoseRecordEntity>>

    @Insert
    suspend fun insert(record: BloodGlucoseRecordEntity)
    @Query("SELECT COUNT(*) FROM bloodglucoserecordentity")
    fun count():Int
}