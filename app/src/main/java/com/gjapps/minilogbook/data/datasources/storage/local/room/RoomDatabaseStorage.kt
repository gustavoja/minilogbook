package com.gjapps.minilogbook.data.datasources.storage.local.room

import com.gjapps.minilogbook.data.datasources.storage.StorageDataSource
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordDao
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordEntity
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordsOverviewDao
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordsOverviewEntity
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomDatabaseStorage(private val bloodGlucoseRecordDao: BloodGlucoseRecordDao, private val bloodGlucoseRecordsOverviewDao: BloodGlucoseRecordsOverviewDao, private val appDatabase: AppDatabase) : StorageDataSource {
    override val bloodGlucoseAverage: Flow<Float>
        get() =  bloodGlucoseRecordsOverviewDao.getFirst()?.map { it?.recordsAverage ?: 0f } ?: flowOf(0f)
    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordModel>>
        get() =  bloodGlucoseRecordDao.getAll().map { items -> items.map { BloodGlucoseRecordModel(it.value, it.date) } }

    override suspend fun saveRecord(
        record: BloodGlucoseRecordModel,
        newAverage: Float,
        newRecordsSum: Float
    ) {
        bloodGlucoseRecordDao.insert(BloodGlucoseRecordEntity(value = record.mgdlValue,date = record.date))

        addOrUpdateOverview(newAverage, newRecordsSum)
    }

    override suspend fun bloodGlucoseRecordsCount(): Int {
        return withContext(Dispatchers.IO){
            bloodGlucoseRecordDao.count()
        }
    }

    override suspend fun getBloodGlucoseRecordsSum(): Float {
        return withContext(Dispatchers.IO){
            bloodGlucoseRecordsOverviewDao.getFirst()?.first()?.recordsSum ?: 0f
        }
    }

    override suspend fun deleteRecords() {
        withContext(Dispatchers.IO) {
            appDatabase.clearAllTables()
        }
    }

    private suspend fun addOrUpdateOverview(newAverage: Float, newRecordsSum: Float) {
        var first = bloodGlucoseRecordsOverviewDao.getFirst()?.firstOrNull()
        if (first == null) {
            first = BloodGlucoseRecordsOverviewEntity(
                recordsAverage = newAverage,
                recordsSum = newRecordsSum
            )
            bloodGlucoseRecordsOverviewDao.insert(first)
        } else {
            var uid = first.uid
            first = first.copy(recordsAverage = newAverage, recordsSum = newRecordsSum)
            first.uid = uid
            bloodGlucoseRecordsOverviewDao.update(first)
        }
    }
}