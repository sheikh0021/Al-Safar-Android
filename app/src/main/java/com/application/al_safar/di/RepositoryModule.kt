package com.application.al_safar.di

import android.content.Context
import com.application.al_safar.data.local.dao.*
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.remote.api.*
import com.application.al_safar.data.remote.interceptors.AuthInterceptor
import com.application.al_safar.data.repository.*
import com.application.al_safar.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(preferencesManager: PreferencesManager): AuthInterceptor {
        return AuthInterceptor(preferencesManager)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        userDao: UserDao,
        preferencesManager: PreferencesManager
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, userDao, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideGuideRepository(
        guideApi: GuideApi,
        umrahStepDao: UmrahStepDao,
        duaDao: DuaDao
    ): GuideRepository {
        return GuideRepositoryImpl(guideApi, umrahStepDao, duaDao)
    }

    @Provides
    @Singleton
    fun provideBookingRepository(
        bookingApi: BookingApi
    ): BookingRepository {
        return BookingRepositoryImpl(bookingApi)
    }

    @Provides
    @Singleton
    fun provideTripRepository(
        tripApi: TripApi,
        tripDao: TripDao,
        preferencesManager: PreferencesManager
    ): TripRepository {
        return TripRepositoryImpl(tripApi, tripDao, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideGroupRepository(
        groupApi: GroupApi,
        groupDao: GroupDao,
        preferencesManager: PreferencesManager
    ): GroupRepository {
        return GroupRepositoryImpl(groupApi, groupDao, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideMapRepository(
        mapApi: MapApi,
        placeDao: PlaceDao
    ): MapRepository {
        return MapRepositoryImpl(mapApi, placeDao)
    }

    @Provides
    @Singleton
    fun provideEmergencyRepository(
        emergencyApi: EmergencyApi
    ): EmergencyRepository {
        return EmergencyRepositoryImpl(emergencyApi)
    }
}
