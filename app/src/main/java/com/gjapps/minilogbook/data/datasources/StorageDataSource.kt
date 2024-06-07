package com.gjapps.minilogbook.data.datasources

import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface StorageDataSource {
    val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
    suspend fun saveRecord(value: BloodGlucoseRecordModel)
    suspend fun reloadBloodGlucoseRecords()
}

class MemoryStorageDataSource:StorageDataSource {
    private var records = mutableListOf<BloodGlucoseRecordModel>()
    private val bloodGlucoseRecordsSharedFlow = MutableSharedFlow<List<BloodGlucoseRecordModel>>(extraBufferCapacity = 1)
    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
        get() = bloodGlucoseRecordsSharedFlow.asSharedFlow()

    override suspend fun saveRecord(value: BloodGlucoseRecordModel) {
        records.add(value)
        bloodGlucoseRecordsSharedFlow.emit(records)
    }
    override suspend fun reloadBloodGlucoseRecords(){
        bloodGlucoseRecordsSharedFlow.emit(records)
    }
}