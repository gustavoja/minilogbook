package com.gjapps.minilogbook.domain.di

import com.gjapps.minilogbook.domain.usecases.ConvertMgDlToMmollUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertMgDlToMmollUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConverMmollToMgDlUseCase
import com.gjapps.minilogbook.domain.usecases.ConverMmollToMgDlUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertBloodGlucoseUnitUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertBloodGlucoseUnitUseCaseUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDateFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ParseFromCurrentLanguageFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ParseFromCurrentLanguageFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCase
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ValidateGlucoseInputUseCase
import com.gjapps.minilogbook.domain.usecases.ValidateGlucoseInputUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDateFormatUseCaseImpl
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
    fun providesSanitizeDecimalNumberUseCase() : SanitizeDecimalNumberUseCase {
        return SanitizeDecimalNumberUseCaseImpl()
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
        return ParseFromCurrentLanguageFormatUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertMmollToMgDlUseCase() : ConverMmollToMgDlUseCase {
        return ConverMmollToMgDlUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConverMgDlToMmollUseCase() : ConvertMgDlToMmollUseCase {
        return ConvertMgDlToMmollUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesValidateGlucoseInputUseCase(sanitizeDecimalNumberUseCase:SanitizeDecimalNumberUseCase) : ValidateGlucoseInputUseCase {
        return ValidateGlucoseInputUseCaseImpl(sanitizeDecimalNumberUseCase)
    }

    @ViewModelScoped
    @Provides
    fun providesConvertBloodGlucoseUnit(converMgDlToMmoll:ConvertMgDlToMmollUseCase,
                                        convertMmollToMgDl:ConverMmollToMgDlUseCase,
                                        convertFromCurrentLanguageDecimalFormat: ParseFromCurrentLanguageFormatUseCase) : ConvertBloodGlucoseUnitUseCase {
        return ConvertBloodGlucoseUnitUseCaseUseCaseImpl(converMgDlToMmoll,convertMmollToMgDl,convertFromCurrentLanguageDecimalFormat)
    }
}