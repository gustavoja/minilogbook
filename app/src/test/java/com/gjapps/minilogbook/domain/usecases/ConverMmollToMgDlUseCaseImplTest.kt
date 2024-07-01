package com.gjapps.minilogbook.domain.usecases

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ConvertMmollToMgDlUseCaseImplTest {
    @Test
    fun whenExecuted_ExpectValueTransformedCorrectly() {
        //act
        val result = ConvertMmollToMgDlUseCaseImpl().invoke(1f)
        //assert
        expectThat(result).isEqualTo(18.0182f)
    }
}