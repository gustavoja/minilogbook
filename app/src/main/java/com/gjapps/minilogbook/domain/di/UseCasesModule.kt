package com.gjapps.minilogbook.domain.di

import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.domain.usecases.ConvertBloodGlucoseUnitUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertBloodGlucoseUnitUseCaseUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertMgDlToMmollUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertMgDlToMmollUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertMmollToMgDlUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertMmollToMgDlUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDateFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDateFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.DecimalSeparatorForCurrentLocaleUseCase
import com.gjapps.minilogbook.domain.usecases.DecimalSeparatorForCurrentLocaleUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.GetBloodGlucoseAverageUseCase
import com.gjapps.minilogbook.domain.usecases.GetBloodGlucoseAverageUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.GetLocalisedBloodGlucoseRecordsUseCase
import com.gjapps.minilogbook.domain.usecases.GetLocalisedBloodGlucoseRecordsUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ParseFromCurrentLanguageFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ParseFromLanguageFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCase
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ValidateGlucoseInputUseCase
import com.gjapps.minilogbook.domain.usecases.ValidateGlucoseInputUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {
    @ViewModelScoped
    @Provides
    fun providesSanitizeDecimalNumberUseCase(decimalSeparatorForCurrentLocaleUseCase: DecimalSeparatorForCurrentLocaleUseCase) : SanitizeDecimalNumberUseCase {
        return SanitizeDecimalNumberUseCaseImpl(decimalSeparatorForCurrentLocaleUseCase)
    }

    @ViewModelScoped
    @Provides
    fun providesDateToLocaleStringFormatUseCase() : ConvertToCurrentLanguageDateFormatUseCase {
        return ConvertToCurrentLanguageDateFormatUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertToCurrentLanguageDecimalFormatUseCase() : ConvertToCurrentLanguageFormatUseCase {
        return ConvertToCurrentLanguageFormatUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertFromCurrentLanguageDecimalFormatUseCase() : ParseFromCurrentLanguageFormatUseCase {
        return ParseFromLanguageFormatUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertMmollToMgDlUseCase() : ConvertMmollToMgDlUseCase {
        return ConvertMmollToMgDlUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertMgDlToMmollUseCase() : ConvertMgDlToMmollUseCase {
        return ConvertMgDlToMmollUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesValidateGlucoseInputUseCase(sanitizeDecimalNumberUseCase:SanitizeDecimalNumberUseCase,decimalSeparatorForCurrentLocaleUseCase:DecimalSeparatorForCurrentLocaleUseCase) : ValidateGlucoseInputUseCase {
        return ValidateGlucoseInputUseCaseImpl(sanitizeDecimalNumberUseCase,decimalSeparatorForCurrentLocaleUseCase)
    }

    @ViewModelScoped
    @Provides
    fun providesConvertBloodGlucoseUnit(convertMgDlToMmoll:ConvertMgDlToMmollUseCase,
                                        convertMmollToMgDl:ConvertMmollToMgDlUseCase,
                                        convertFromCurrentLanguageDecimalFormat: ParseFromCurrentLanguageFormatUseCase) : ConvertBloodGlucoseUnitUseCase {
        return ConvertBloodGlucoseUnitUseCaseUseCaseImpl(convertMgDlToMmoll,convertMmollToMgDl,convertFromCurrentLanguageDecimalFormat)
    }

    @ViewModelScoped
    @Provides
    fun providesDecimalSeparatorForCurrentLocaleUseCase() : DecimalSeparatorForCurrentLocaleUseCase {
        return DecimalSeparatorForCurrentLocaleUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesGetLocalisedBloodGlucoseRecordsUseCase( bloodGlucoseRecordsRepository: BloodGlucoseRepository,
                                                        convertToCurrentLanguageDateFormat: ConvertToCurrentLanguageDateFormatUseCase,
                                                        convertToCurrentLanguageDecimalFormat: ConvertToCurrentLanguageFormatUseCase,
                                                        convertBloodGlucoseUnit : ConvertBloodGlucoseUnitUseCase) : GetLocalisedBloodGlucoseRecordsUseCase {
        return GetLocalisedBloodGlucoseRecordsUseCaseImpl(bloodGlucoseRecordsRepository,convertToCurrentLanguageDateFormat,convertToCurrentLanguageDecimalFormat,convertBloodGlucoseUnit)
    }

    @ViewModelScoped
    @Provides
    fun providesGetBloodGlucoseAverageUseCase( bloodGlucoseRecordsRepository: BloodGlucoseRepository,
                                               convertToCurrentLanguageDecimalFormat: ConvertToCurrentLanguageFormatUseCase,
                                               convertBloodGlucoseUnit : ConvertBloodGlucoseUnitUseCase) : GetBloodGlucoseAverageUseCase {
        return GetBloodGlucoseAverageUseCaseImpl(bloodGlucoseRecordsRepository,convertToCurrentLanguageDecimalFormat,convertBloodGlucoseUnit)
    }
}