package com.gjapps.minilogbook.data.datasources.storage

import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordEntity
import kotlinx.coroutines.flow.Flow

interface StorageDataSource {
    val bloodGlucoseAverage: Flow<Float>
    val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordEntity>>
    suspend fun saveRecord(record: BloodGlucoseRecordEntity, newAverage:Float, newRecordsSum:Float, newRecordsCount:Int)
    suspend fun getBloodGlucoseRecordsCount():Int
    suspend fun getBloodGlucoseRecordsSum():Float
    suspend fun deleteRecords()
}