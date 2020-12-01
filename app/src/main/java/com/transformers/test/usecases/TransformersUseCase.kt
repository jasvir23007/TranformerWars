package com.transformers.test.usecases

import com.transformers.test.models.transformers.AllTransformers
import com.transformers.test.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class TransformersUseCase : KoinComponent {

    private val mRepo : Repository by inject()

    suspend fun processTransformersUseCase() : AllTransformers {
        return mRepo.getTransformersData()
    }
}