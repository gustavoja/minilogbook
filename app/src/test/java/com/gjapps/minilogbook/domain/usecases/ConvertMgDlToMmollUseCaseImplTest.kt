package com.gjapps.minilogbook.domain.usecases

import org.junit.Test
import strikt.api.expectThat

class ConvertMgDlToMmollUseCaseImplTest {
    @Test
    fun whenExecuted_ExpectValueTransformedCorrectly() {
        //act
        val result = ConvertMgDlToMmollUseCaseImpl().invoke(18.0182f)
        //assert
        expectThat(result).equals(1)
    }
}