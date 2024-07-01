package com.gjapps.minilogbook.domain.usecases

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ConvertMgDlToMmollUseCaseImplTest {
    @Test
    fun whenExecuted_ExpectValueTransformedCorrectly() {
        //act
        val result = ConvertMgDlToMmollUseCaseImpl().invoke(18.0182f)
        //assert
        expectThat(result).isEqualTo(1f)
    }
}