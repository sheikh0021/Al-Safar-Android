package com.application.al_safar.di

import com.application.al_safar.data.remote.api.*
import com.application.al_safar.data.remote.interceptors.AuthInterceptor
import com.application.al_safar.util.ApiConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson{
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    fun provideGuideApi(retrofit: Retrofit): GuideApi = retrofit.create(GuideApi::class.java)

    @Provides
    fun provideTripApi(retrofit: Retrofit): TripApi = retrofit.create(TripApi::class.java)

    @Provides
    fun provideMapApi(retrofit: Retrofit): MapApi = retrofit.create(MapApi::class.java)

    @Provides
    fun provideGroupApi(retrofit: Retrofit): GroupApi = retrofit.create(GroupApi::class.java)

    @Provides
    fun provideEmergencyApi(retrofit: Retrofit): EmergencyApi =
        retrofit.create(EmergencyApi::class.java)

    @Provides
    fun provideBookingApi(retrofit: Retrofit): BookingApi = retrofit.create(BookingApi::class.java)
}