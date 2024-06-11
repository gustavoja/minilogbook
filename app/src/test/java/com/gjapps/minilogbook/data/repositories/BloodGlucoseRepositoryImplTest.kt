package com.gjapps.minilogbook.data.repositories

import com.gjapps.minilogbook.data.datasources.storage.StorageDataSource
import com.gjapps.minilogbook.data.datasources.storage.local.room.daos.BloodGlucoseRecordEntity
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat

class BloodGlucoseRepositoryImplTest {
    
    private lateinit var repository: BloodGlucoseRepository
    private var storageDataSourceMock = Mockito.mock(StorageDataSource::class.java)
    
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = BloodGlucoseRepositoryImpl(storageDataSourceMock)
    }

    @Test
    fun whenSaveRecordExecuted_ExpectValueSavedOnDatabase()  = runTest {
        //arrange
        `when`(storageDataSourceMock.getBloodGlucoseRecordsSum()).thenReturn(1f)
        `when`(storageDataSourceMock.getBloodGlucoseRecordsCount()).thenReturn(1)

        //act
        repository.saveRecord(1f)

        //assert
        verify(storageDataSourceMock, Mockito.times(1)).saveRecord(any(),any(),any(),any())
    }

    @Test
    fun whenDeleteRecordsExecuted_ExpectDeleteRecordsOnDatabase() = runTest {
        //act
        repository.deleteRecords()

        //assert
        verify(storageDataSourceMock, Mockito.times(1)).deleteRecords()
    }

    @Test
    fun whenGeyBloodGlucoseAverage_emitsExpectedValue() = runTest {
        //arrange
        val expectedAverage = 100.0f
        whenever(storageDataSourceMock.bloodGlucoseAverage).thenReturn(flowOf(expectedAverage))
        repository = BloodGlucoseRepositoryImpl(storageDataSourceMock)

        //act
        val actualAverage = repository.bloodGlucoseAverage.first()

        //assert
        expectedAverage.equals(actualAverage)
    }

    @Test
    fun whenGetBloodGlucoseRecords_emitsExpectedValue() = runTest {
        //arrange
        val date0 = "2023-12-18T09:30:00"
        val date1 = "2024-12-18T10:30:00"
        val date2 = "2025-12-18T10:30:00"

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val dateTime1 = formatter.parse(date0)
        val dateTime2 = formatter.parse(date1)
        val dateTime3 = formatter.parse(date2)

        whenever(storageDataSourceMock.bloodGlucoseRecords).thenReturn(flowOf(listOf(
            BloodGlucoseRecordEntity(120.0f, dateTime1),
            BloodGlucoseRecordEntity(90.0f, dateTime2),
            BloodGlucoseRecordEntity(100.0f, dateTime3 ),
        )))

        val expectedRecords = listOf(
            BloodGlucoseRecordModel(100.0f, dateTime3),
            BloodGlucoseRecordModel(90.0f,dateTime2),
            BloodGlucoseRecordModel(120.0f, dateTime1),
        )

        repository = BloodGlucoseRepositoryImpl(storageDataSourceMock)

        //act
        val actualRecords = repository.bloodGlucoseRecords.first()

        //assert
        assertEquals(expectedRecords, actualRecords)
    }
}