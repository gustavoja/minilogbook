package com.gjapps.minilogbook.data.repositories

import com.gjapps.minilogbook.data.datasources.StorageDataSource
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import com.gjapps.minilogbook.extensions.fromUTC
import com.gjapps.minilogbook.extensions.toUTC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

interface BloodGlucoseRepository {
    val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
    suspend fun saveRecord(value: Float)
}

class BloodGlucoseRepositoryImpl @Inject constructor(private val storageDataSource: StorageDataSource): BloodGlucoseRepository {
    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>> =
        storageDataSource.getRecords()
            .map { it.sortedByDescending { it.date }.map { it.copy(date = it.date.fromUTC()) } }

    override suspend fun saveRecord(value: Float) {
        val record = BloodGlucoseRecordModel(value,Date().toUTC())
        storageDataSource.saveRecord(record)
    }
}