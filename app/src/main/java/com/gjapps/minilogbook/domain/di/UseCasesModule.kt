package com.gjapps.minilogbook.domain.di

import com.gjapps.minilogbook.domain.usecases.ConvertFloatToLocaleDecimalStringUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertFloatToLocaleDecimalStringUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertLocaleDecimalStringFloatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertLocaleDecimalStringFloatUseCaseImpl
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
    fun providesConvertLocaleDecimalStringFloatUseCaseImpl() : ConvertLocaleDecimalStringFloatUseCase {
        return ConvertLocaleDecimalStringFloatUseCaseImpl()
    }

    @ViewModelScoped
    @Provides
    fun providesConvertFloatToLocaleDecimalStringUseCase() : ConvertFloatToLocaleDecimalStringUseCase {
        return ConvertFloatToLocaleDecimalStringUseCaseImpl()
    }
}