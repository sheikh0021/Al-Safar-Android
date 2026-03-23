package com.application.al_safar.domain.usecase.guide

import com.application.al_safar.domain.model.Dua
import com.application.al_safar.domain.repository.GuideRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDuasUseCase @Inject constructor(private val repository: GuideRepository) {
    operator fun invoke(category: String? = null): Flow<List<Dua>> {
        return repository.getDuas(category)
    }
}
