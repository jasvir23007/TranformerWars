package com.transformers.test.usecases

import com.transformers.test.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class DeleteTransformerUseCase : KoinComponent {

    private val mRepo : Repository by inject()

    suspend fun processdeleteTransformerUseCase(id: String): Boolean {
        return mRepo.deleteTransformers(id)
    }
}