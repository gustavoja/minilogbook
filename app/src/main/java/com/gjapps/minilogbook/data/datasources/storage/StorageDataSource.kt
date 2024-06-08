package com.gjapps.minilogbook.data.datasources.storage

import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import kotlinx.coroutines.flow.Flow

interface StorageDataSource {
    val bloodGlucoseAverage: Flow<Float>
    val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
    suspend fun saveRecord(record: BloodGlucoseRecordModel, newAverage:Float, newRecordsSum:Float, newRecordsCount:Int)
    suspend fun getBloodGlucoseRecordsCount():Int
    suspend fun getBloodGlucoseRecordsSum():Float
    suspend fun deleteRecords()
}