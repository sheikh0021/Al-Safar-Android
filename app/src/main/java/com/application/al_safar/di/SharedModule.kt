package com.application.al_safar.di

import com.application.al_safar.core.SessionStorage
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.remote.AndroidNetwork
import com.application.al_safar.data.remote.AuthRemoteDataSource
import com.application.al_safar.domain.repository.AuthRepository
import com.application.al_safar.domain.usecase.auth.LoginUseCase
import com.application.al_safar.domain.usecase.auth.LogoutUseCase
import com.application.al_safar.domain.usecase.auth.SignupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Provides
    @Singleton
    fun provideSessionStorage(preferencesManager: PreferencesManager): SessionStorage {
        return preferencesManager
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(): AuthRemoteDataSource {
        return AndroidNetwork.createAuthRemoteDataSource()
    }

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideSignupUseCase(repository: AuthRepository): SignupUseCase {
        return SignupUseCase(repository)
    }

    @Provides
    fun provideLogoutUseCase(repository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(repository)
    }
}
