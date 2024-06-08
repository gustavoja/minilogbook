package com.gjapps.minilogbook.data.repositories

import com.gjapps.minilogbook.data.datasources.storage.StorageDataSource
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import com.gjapps.minilogbook.extensions.fromUTC
import com.gjapps.minilogbook.extensions.toUTC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

interface BloodGlucoseRepository {
    val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
    val bloodGlucoseAverage: Flow<Float>
    suspend fun saveRecord(mgdlValue: Float)

    suspend fun reloadBloodGlucoseRecords()
}

class BloodGlucoseRepositoryImpl @Inject constructor(private val storageDataSource: StorageDataSource): BloodGlucoseRepository {
    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>> =
        storageDataSource.bloodGlucoseRecords
            .map { it.sortedByDescending { it.date }.map { it.copy(date = it.date.fromUTC()) } }

    override val bloodGlucoseAverage: Flow<Float> = storageDataSource.bloodGlucoseAverage

    override suspend fun saveRecord(mgdlValue: Float) {
        val record = BloodGlucoseRecordModel(mgdlValue,Date().toUTC())
        val recordsSum = storageDataSource.getBloodGlucoseRecordsSum() + mgdlValue
        val average = recordsSum/(storageDataSource.bloodGlucoseRecordsCount()+1)
        storageDataSource.saveRecord(record,average,recordsSum)
    }

    override suspend fun reloadBloodGlucoseRecords() {
        storageDataSource.reloadBloodGlucoseRecords()
    }
}