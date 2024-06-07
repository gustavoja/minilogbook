package com.gjapps.minilogbook.data.di

import com.gjapps.minilogbook.data.datasources.MemoryStorageDataSource
import com.gjapps.minilogbook.data.datasources.StorageDataSource
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun providesStorageDataSource() : StorageDataSource {
        return MemoryStorageDataSource()
    }

    @Singleton
    @Provides
    fun providesBloodGlucoseRepository(storageDataSource: StorageDataSource) : BloodGlucoseRepository {
        return BloodGlucoseRepositoryImpl(storageDataSource)
    }
}