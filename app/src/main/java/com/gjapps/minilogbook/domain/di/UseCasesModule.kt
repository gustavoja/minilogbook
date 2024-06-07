package com.gjapps.minilogbook.domain.di

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
}