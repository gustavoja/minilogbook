package com.gjapps.minilogbook.data.datasources

import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

interface StorageDataSource {
    val bloodGlucoseAverage: StateFlow<Float>
    val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
    suspend fun saveRecord(value: BloodGlucoseRecordModel, newAverage:Float)
    suspend fun reloadBloodGlucoseRecords()
    suspend fun bloodGlucoseRecordsCount():Int
}

class MemoryStorageDataSource() :StorageDataSource {
    private var records = mutableListOf<BloodGlucoseRecordModel>()
    private val _bloodGlucoseRecords = MutableSharedFlow<List<BloodGlucoseRecordModel>>(extraBufferCapacity = 1)
    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
        get() = _bloodGlucoseRecords.asSharedFlow()

    private val _bloodGlucoseAverage = MutableStateFlow(0f)
    override val bloodGlucoseAverage: StateFlow<Float>
        get() = _bloodGlucoseAverage.asStateFlow()

    override suspend fun saveRecord(value: BloodGlucoseRecordModel, newAverage:Float) {
        records.add(value)
        _bloodGlucoseRecords.emit(records)
        _bloodGlucoseAverage.emit(newAverage)
    }
    override suspend fun reloadBloodGlucoseRecords(){
        _bloodGlucoseRecords.emit(records)
    }

    override suspend fun bloodGlucoseRecordsCount():Int{
        return records.count()
    }
}