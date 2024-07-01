package com.gjapps.minilogbook.data.repositories

import com.gjapps.minilogbook.data.datasources.storage.StorageDataSource
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordEntity
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

interface BloodGlucoseRepository {
    val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
    val bloodGlucoseAverage: Flow<Float>
    suspend fun saveRecord(mgdlValue: Float)
    suspend fun deleteRecords()
}

class BloodGlucoseRepositoryImpl @Inject constructor(private val storageDataSource: StorageDataSource): BloodGlucoseRepository {
    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>> =
        storageDataSource.bloodGlucoseRecords
            .map { items -> items.map { BloodGlucoseRecordModel(it.value, it.date) } }
            .map { items -> items.sortedByDescending { it.date }}

    override val bloodGlucoseAverage: Flow<Float> = storageDataSource.bloodGlucoseAverage

    override suspend fun saveRecord(mgdlValue: Float) {
        val record = BloodGlucoseRecordEntity(mgdlValue,Date())
        val (average,recordsSum,recordsCount) = calculateAverage(mgdlValue)
        storageDataSource.saveRecord(record,average,recordsSum,recordsCount)
    }

    override suspend fun deleteRecords() {
        storageDataSource.deleteRecords()
    }

    private suspend fun calculateAverage(mgdlValue: Float): Triple<Float,Float,Int>{
        val recordsSum = storageDataSource.getBloodGlucoseRecordsSum() + mgdlValue
        val recordsCount = storageDataSource.getBloodGlucoseRecordsCount() + 1
        return Triple(recordsSum/recordsCount,recordsSum,recordsCount)
    }
}