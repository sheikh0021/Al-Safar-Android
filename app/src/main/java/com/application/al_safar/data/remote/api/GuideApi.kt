package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.guide.DuaDto
import com.application.al_safar.data.remote.dto.guide.UmrahStepDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GuideApi {
    @GET("guide/umrah-steps/")
    suspend fun getUmrahSteps(): Response<List<UmrahStepDto>>

    @GET("guide/umrah-steps/{id}")
    suspend fun getUmrahStepById(@Path("id") id: String): Response<UmrahStepDto>

    @GET("guide/umrah-steps/{id}/complete/")
    suspend fun markStepComplete(@Path("id") id: String): Response<Unit>

    @GET("guide/duas/")
    suspend fun getDuas(@Query("category") category: String? = null): Response<List<DuaDto>>

    @GET("guide/duas/{id}/")
    suspend fun getDuaById(@Path("id") id: String): Response<DuaDto>
}