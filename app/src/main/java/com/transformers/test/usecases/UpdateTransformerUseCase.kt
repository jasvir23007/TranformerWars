package com.transformers.test.usecases

import com.transformers.test.models.transformers.Transformer
import com.transformers.test.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class UpdateTransformerUseCase : KoinComponent {

    private val mRepo : Repository by inject()

    suspend fun processTransformersUseCase(transformer: Transformer) : Transformer {
        return mRepo.updateTransformer(transformer)
    }
}