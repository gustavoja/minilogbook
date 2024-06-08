package com.gjapps.minilogbook.data.datasources.storage.local.memory

import com.gjapps.minilogbook.data.datasources.storage.StorageDataSource
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class MemoryStorage : StorageDataSource {
    private var records = mutableListOf<BloodGlucoseRecordModel>()
    private val _bloodGlucoseRecords = MutableSharedFlow<List<BloodGlucoseRecordModel>>(extraBufferCapacity = 1,replay = 1)
    private var _bloodGlucoseRecordsSum = 0f

    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
        get() = _bloodGlucoseRecords.asSharedFlow()

    private val _bloodGlucoseAverage = MutableStateFlow(0f)
    override val bloodGlucoseAverage: StateFlow<Float>
        get() = _bloodGlucoseAverage.asStateFlow()

    override suspend fun saveRecord(value: BloodGlucoseRecordModel, newAverage:Float, newRecordsSum:Float) {
        records.add(value)
        _bloodGlucoseRecordsSum = newRecordsSum
        _bloodGlucoseRecords.emit(records)
        _bloodGlucoseAverage.emit(newAverage)
    }

    override suspend fun bloodGlucoseRecordsCount():Int{
        return records.count()
    }

    override suspend fun getBloodGlucoseRecordsSum(): Float {
        return  _bloodGlucoseRecordsSum
    }
}
