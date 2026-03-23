package com.application.al_safar.domain.repository

import com.application.al_safar.domain.model.Dua
import com.application.al_safar.domain.model.UmrahStep
import kotlinx.coroutines.flow.Flow


interface GuideRepository {
    fun getUmrahSteps(): Flow<List<UmrahStep>>
    suspend fun syncUmrahSteps()
    fun getDuas(category: String?): Flow<List<Dua>>
    suspend fun syncDuas(category: String?)

}