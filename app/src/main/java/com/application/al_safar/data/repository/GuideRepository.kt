package com.application.al_safar.data.repository

import com.application.al_safar.data.local.dao.DuaDao
import com.application.al_safar.data.local.dao.UmrahStepDao
import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.mapper.toEntity
import com.application.al_safar.data.remote.api.GuideApi
import com.application.al_safar.domain.model.Dua
import com.application.al_safar.domain.model.UmrahStep
import com.application.al_safar.domain.repository.GuideRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GuideRepositoryImpl(
    private val guideApi: GuideApi,
    private val umrahStepDao: UmrahStepDao,
    private val duaDao: DuaDao
) : GuideRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun getUmrahSteps(): Flow<List<UmrahStep>> {
        scope.launch { syncUmrahSteps() }
        return umrahStepDao.getAllSteps().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncUmrahSteps() {
        try {
            val response = guideApi.getUmrahSteps()
            if (response.isSuccessful && response.body() != null) {
                umrahStepDao.insertSteps(response.body()!!.map { it.toDomain().toEntity() })
            }
        } catch (e: Exception) {
            // Use cached data on network failure
        }
    }

    override fun getDuas(category: String?): Flow<List<Dua>> {
        scope.launch { syncDuas(category) }
        return if (category != null) {
            duaDao.getDuasByCategory(category).map { entities ->
                entities.map { it.toDomain() }
            }
        } else {
            duaDao.getAllDuas().map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }

    override suspend fun syncDuas(category: String?) {
        try {
            val response = guideApi.getDuas(category)
            if (response.isSuccessful && response.body() != null) {
                duaDao.insertDuas(response.body()!!.map { it.toDomain().toEntity() })
            }
        } catch (e: Exception) {
            // Use cached data on network failure
        }
    }
}
