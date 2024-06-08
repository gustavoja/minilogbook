package com.gjapps.minilogbook.data.datasources.storage.local.room.daos

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "BloodGlucoseRecordsOverviewEntity")
data class BloodGlucoseRecordsOverviewEntity(
    val recordsAverage: Float,
    val recordsSum: Float,
    val recordsCount: Int
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface BloodGlucoseRecordsOverviewDao {
    @Query("SELECT * FROM BloodGlucoseRecordsOverviewEntity LIMIT 1;")
    fun getFirst(): Flow<BloodGlucoseRecordsOverviewEntity>

    @Insert
    suspend fun insert(overview: BloodGlucoseRecordsOverviewEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(overview: BloodGlucoseRecordsOverviewEntity)
}