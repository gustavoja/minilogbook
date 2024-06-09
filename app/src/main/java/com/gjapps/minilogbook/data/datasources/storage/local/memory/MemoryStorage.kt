package com.gjapps.minilogbook.data.datasources.storage.local.memory

import com.gjapps.minilogbook.data.datasources.storage.StorageDataSource
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class MemoryStorage : StorageDataSource {
    private var records = mutableListOf<BloodGlucoseRecordEntity>()
    private val _bloodGlucoseRecords = MutableSharedFlow<List<BloodGlucoseRecordEntity>>(extraBufferCapacity = 1,replay = 1)
    private var _bloodGlucoseRecordsSum = 0f
    private var _bloodGlucoseRecordsCount = 0

    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordEntity>>
        get() = _bloodGlucoseRecords.asSharedFlow()

    private val _bloodGlucoseAverage = MutableStateFlow(0f)
    override val bloodGlucoseAverage: StateFlow<Float>
        get() = _bloodGlucoseAverage.asStateFlow()

    override suspend fun saveRecord(value: BloodGlucoseRecordEntity, newAverage:Float, newRecordsSum:Float,newRecordsCount:Int) {
        records.add(value)
        _bloodGlucoseRecordsSum = newRecordsSum
        _bloodGlucoseRecordsCount = newRecordsCount
        _bloodGlucoseRecords.emit(records)
        _bloodGlucoseAverage.emit(newAverage)
    }

    override suspend fun getBloodGlucoseRecordsCount():Int{
        return _bloodGlucoseRecordsCount
    }

    override suspend fun getBloodGlucoseRecordsSum(): Float {
        return  _bloodGlucoseRecordsSum
    }

    override suspend fun deleteRecords() {
        records.clear()
    }
}
