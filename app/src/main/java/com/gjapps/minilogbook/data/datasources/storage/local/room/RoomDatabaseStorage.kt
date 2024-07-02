package com.gjapps.minilogbook.data.datasources.storage.local.room

import com.gjapps.minilogbook.data.datasources.storage.StorageDataSource
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordDao
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordEntity
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordsOverviewDao
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordsOverviewEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomDatabaseStorage(private val bloodGlucoseRecordDao: BloodGlucoseRecordDao, private val bloodGlucoseRecordsOverviewDao: BloodGlucoseRecordsOverviewDao, private val appDatabase: AppDatabase) : StorageDataSource {
    override val bloodGlucoseAverage: Flow<Float?>
        get() = bloodGlucoseRecordsOverviewDao.getFirst().map { it?.recordsAverage }
    override val bloodGlucoseRecords: Flow<List<BloodGlucoseRecordEntity>>
        get() =  bloodGlucoseRecordDao.getAll()

    override suspend fun saveRecord(
        record: BloodGlucoseRecordEntity,
        newAverage: Float,
        newRecordsSum: Float,
        newRecordsCount:Int
    ) {
        bloodGlucoseRecordDao.insert(record)

        addOrUpdateOverview(newAverage, newRecordsSum, newRecordsCount)
    }

    override suspend fun getBloodGlucoseRecordsCount(): Int {
        return withContext(Dispatchers.IO){
            bloodGlucoseRecordsOverviewDao.getFirst().firstOrNull()?.recordsCount ?: 0
        }
    }

    override suspend fun getBloodGlucoseRecordsSum(): Float {
        return withContext(Dispatchers.IO){
            bloodGlucoseRecordsOverviewDao.getFirst().firstOrNull()?.recordsSum ?: 0f
        }
    }

    override suspend fun deleteRecords() {
        withContext(Dispatchers.IO) {
            appDatabase.clearAllTables()
        }
    }

    private suspend fun addOrUpdateOverview(newAverage: Float, newRecordsSum: Float, newRecordsCount:Int) {
        var first = bloodGlucoseRecordsOverviewDao.getFirst().firstOrNull()
        if (first == null) {
            first = BloodGlucoseRecordsOverviewEntity(
                recordsAverage = newAverage,
                recordsSum = newRecordsSum,
                recordsCount = newRecordsCount
            )
            bloodGlucoseRecordsOverviewDao.insert(first)
        } else {
            val uid = first.uid
            first = first.copy(recordsAverage = newAverage, recordsSum = newRecordsSum, recordsCount = newRecordsCount)
            first.uid = uid
            bloodGlucoseRecordsOverviewDao.update(first)
        }
    }
}