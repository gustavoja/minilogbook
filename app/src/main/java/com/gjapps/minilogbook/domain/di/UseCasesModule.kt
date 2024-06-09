package com.gjapps.minilogbook.domain.di

import com.gjapps.minilogbook.domain.usecases.ConverMgDlToMmollUseCase
import com.gjapps.minilogbook.domain.usecases.ConverMgDlToMmollUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConverMmollToMgDlUseCase
import com.gjapps.minilogbook.domain.usecases.ConverMmollToMgDlUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertFromCurrentLanguageDecimalFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertFromCurrentLanguageDecimalFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDecimalFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDecimalFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.DateToLocaleStringFormatUseCase
import com.gjapps.minilogbook.domain.usecases.DateToLocaleStringFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCase
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCaseImpl
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
    fun providesDateToLocaleStringFormatUseCase() : DateToLocaleStringFormatUseCase {
        return DateToLocaleStringFormatUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertToCurrentLanguageDecimalFormatUseCase() : ConvertToCurrentLanguageDecimalFormatUseCase {
        return ConvertToCurrentLanguageDecimalFormatUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertFromCurrentLanguageDecimalFormatUseCase() : ConvertFromCurrentLanguageDecimalFormatUseCase {
        return ConvertFromCurrentLanguageDecimalFormatUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertMmollToMgDlUseCase() : ConverMmollToMgDlUseCase {
        return ConverMmollToMgDlUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConverMgDlToMmollUseCase() : ConverMgDlToMmollUseCase {
        return ConverMgDlToMmollUseCaseImpl()
    }

}