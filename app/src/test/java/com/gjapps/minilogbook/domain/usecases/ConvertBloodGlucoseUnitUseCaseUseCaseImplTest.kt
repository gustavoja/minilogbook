package com.gjapps.minilogbook.domain.usecases

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.Locale

class ConvertBloodGlucoseUnitUseCaseUseCaseImplTest{
private lateinit var convertMgDlToMmoll: ConvertMgDlToMmollUseCase
private lateinit var convertMmollToMgDl: ConvertMmollToMgDlUseCase
private lateinit var convertFromCurrentLanguageDecimalFormat: ParseFromCurrentLanguageFormatUseCase
private lateinit var useCase: ConvertBloodGlucoseUnitUseCaseUseCaseImpl

@Before
fun setup() {
    convertMgDlToMmoll = mock()
    convertMmollToMgDl = mock()
    convertFromCurrentLanguageDecimalFormat = mock()
    useCase = ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
        convertMgDlToMmoll,
        convertMmollToMgDl,
        convertFromCurrentLanguageDecimalFormat
    )
}

@Test
fun whenUseCaseExecuted_WithMgdlToMgdl_ExpectReturnTheSameValue() {
    val inputValue = 100f
    val fromUnit = BloodGlucoseUnit.Mgdl
    val toUnit = BloodGlucoseUnit.Mgdl

    val result = useCase(inputValue, fromUnit, toUnit)

    expectThat(inputValue).isEqualTo(result)
}

@Test
fun whenUseCaseExecuted_WithMmolToMmol_ExpectReturnTheSameValue() {
    val inputValue = 5.5f
    val fromUnit = BloodGlucoseUnit.Mmoldl
    val toUnit = BloodGlucoseUnit.Mmoldl

    val result = useCase(inputValue, fromUnit, toUnit)

    expectThat(inputValue).isEqualTo(result)
}

@Test
fun whenUseCaseExecuted_WithMgdlToMmol_ExpectCallsConvertMgDlToMmoll() {
    val inputValue = 100f
    val fromUnit = BloodGlucoseUnit.Mgdl
    val toUnit = BloodGlucoseUnit.Mmoldl
    val expectedResult = 5.5f

    whenever(convertMgDlToMmoll(inputValue)).thenReturn(expectedResult)

    val result = useCase(inputValue, fromUnit, toUnit)

    expectThat(expectedResult).isEqualTo(result)
}

@Test
fun whenUseCaseExecuted_WithMgdlToMmol_ExpectCallsConvertMmollToMgDl() {
    val inputValue = 5.5f
    val fromUnit = BloodGlucoseUnit.Mmoldl
    val toUnit = BloodGlucoseUnit.Mgdl
    val expectedResult = 100f

    whenever(convertMmollToMgDl(inputValue)).thenReturn(expectedResult)

    val result = useCase(inputValue, fromUnit, toUnit)

    expectThat(expectedResult).isEqualTo(result)
}

@Test
fun whenUseCaseExecuted_WithMgdlToMmol_ExpectCallsParseFromCurrentLanguageFormatAndConverts() {
    val inputValue = "100,5"
    val fromUnit = BloodGlucoseUnit.Mgdl
    val toUnit = BloodGlucoseUnit.Mmoldl
    val parsedValue = 100.5f
    val expectedResult = 5f

    whenever(convertFromCurrentLanguageDecimalFormat.invoke(inputValue, Locale.getDefault())).thenReturn(parsedValue)
    whenever(convertMgDlToMmoll(parsedValue)).thenReturn(expectedResult)

    val result = useCase(inputValue, fromUnit, toUnit)

    expectThat(expectedResult).isEqualTo(result)
}
}