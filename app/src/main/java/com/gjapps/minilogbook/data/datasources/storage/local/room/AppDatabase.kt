package com.gjapps.minilogbook.data.datasources.storage.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gjapps.minilogbook.data.datasources.storage.converters.DateTypeConverter
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordDao
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordEntity
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordsOverviewDao
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordsOverviewEntity


@Database(entities = [BloodGlucoseRecordEntity::class, BloodGlucoseRecordsOverviewEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bloodGlucoseRecordEntityDao(): BloodGlucoseRecordDao
    abstract fun bloodBloodGlucoseRecordsOverviewDao(): BloodGlucoseRecordsOverviewDao
}