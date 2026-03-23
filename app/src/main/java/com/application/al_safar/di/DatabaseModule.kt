package com.application.al_safar.di

import android.content.Context
import androidx.room.Room
import com.application.al_safar.data.local.database.AlSafarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AlSafarDatabase {
        return Room.databaseBuilder(
            context,
            AlSafarDatabase::class.java,
            "al_safar_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AlSafarDatabase) = database.userDao()

    @Provides
    fun provideUmrahStepDao(database: AlSafarDatabase) = database.umrahStepDao()

    @Provides
    fun provideDuaDao(database: AlSafarDatabase) = database.duaDao()

    @Provides
    fun provideTripDao(database: AlSafarDatabase) = database.tripDao()

    @Provides
    fun providePlaceDao(database: AlSafarDatabase) = database.placeDao()

    @Provides
    fun provideGroupDao(database: AlSafarDatabase) = database.groupDao()
}