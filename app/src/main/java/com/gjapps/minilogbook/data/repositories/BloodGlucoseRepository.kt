package com.gjapps.minilogbook.data.repositories

import com.gjapps.minilogbook.data.datasources.StorageDataSource
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import com.gjapps.minilogbook.extensions.fromUTC
import com.gjapps.minilogbook.extensions.toUTC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

interface BloodGlucoseRepository {
    val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
    val bloodGlucoseAverage: StateFlow<Float>
    suspend fun saveRecord(mgdlValue: Float)

    suspend fun reloadBloodGlucoseRecords()
}

class BloodGlucoseRepositoryImpl @Inject constructor(private val storageDataSource: StorageDataSource): BloodGlucoseRepository {
    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>> =
        storageDataSource.bloodGlucoseRecords
            .map { it.sortedByDescending { it.date }.map { it.copy(date = it.date.fromUTC()) } }

    override val bloodGlucoseAverage: StateFlow<Float> = storageDataSource.bloodGlucoseAverage

    override suspend fun saveRecord(mgdlValue: Float) {
        val record = BloodGlucoseRecordModel(mgdlValue,Date().toUTC())
        val average = (storageDataSource.bloodGlucoseAverage.value + mgdlValue)/(storageDataSource.bloodGlucoseRecordsCount()+1)
        storageDataSource.saveRecord(record,average)
    }

    override suspend fun reloadBloodGlucoseRecords() {
        storageDataSource.reloadBloodGlucoseRecords()
    }
}