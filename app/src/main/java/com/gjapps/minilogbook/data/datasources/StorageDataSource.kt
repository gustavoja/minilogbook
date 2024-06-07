package com.gjapps.minilogbook.data.datasources

import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface StorageDataSource {
    suspend fun saveRecord(value: BloodGlucoseRecordModel)
    fun getRecords(): Flow<List<BloodGlucoseRecordModel>>
}

class MemoryStorageDataSource:StorageDataSource {
    private var records = mutableListOf<BloodGlucoseRecordModel>()
    override suspend fun saveRecord(value: BloodGlucoseRecordModel) {
        records.add(value)
        sharedFlow.emit(records)
    }

    private val sharedFlow = MutableSharedFlow<List<BloodGlucoseRecordModel>>(extraBufferCapacity = 1)
    override fun getRecords(): SharedFlow<List<BloodGlucoseRecordModel>> = sharedFlow.asSharedFlow()
}