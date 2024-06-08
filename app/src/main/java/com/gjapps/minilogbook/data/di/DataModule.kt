package com.gjapps.minilogbook.data.di

import android.content.Context
import androidx.room.Room
import com.gjapps.minilogbook.data.datasources.storage.StorageDataSource
import com.gjapps.minilogbook.data.datasources.storage.local.room.AppDatabase
import com.gjapps.minilogbook.data.datasources.storage.local.room.RoomDatabaseStorage
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordDao
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordsOverviewDao
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideBloodGlucoseRecordsOverviewDao(appDatabase: AppDatabase): BloodGlucoseRecordsOverviewDao {
        return appDatabase.bloodBloodGlucoseRecordsOverviewDao()
    }

    @Provides
    fun provideBloodGlucoseRecordEntity(appDatabase: AppDatabase): BloodGlucoseRecordDao {
        return appDatabase.bloodGlucoseRecordEntityDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "MainDatabase"
        ).build()
    }

    @Singleton
    @Provides
    fun providesStorageDataSource(bloodGlucoseRecordDao:BloodGlucoseRecordDao, bloodGlucoseRecordsOverviewDao:BloodGlucoseRecordsOverviewDao) : StorageDataSource {
        //return MemoryStorage()
        return RoomDatabaseStorage(bloodGlucoseRecordDao,bloodGlucoseRecordsOverviewDao)
    }

    @Singleton
    @Provides
    fun providesBloodGlucoseRepository(storageDataSource: StorageDataSource) : BloodGlucoseRepository {
        return BloodGlucoseRepositoryImpl(storageDataSource)
    }
}