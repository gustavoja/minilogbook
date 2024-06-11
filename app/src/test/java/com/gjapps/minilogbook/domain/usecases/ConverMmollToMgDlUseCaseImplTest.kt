package com.gjapps.minilogbook.domain.usecases

import org.junit.Test
import strikt.api.expectThat

class ConvertMmollToMgDlUseCaseImplTest {
    @Test
    fun whenExecuted_ExpectValueTransformedCorrectly() {
        //act
        val result = ConvertMgDlToMmollUseCaseImpl().invoke(1f)
        //assert
        expectThat(result).equals(18.0182f)
    }
}