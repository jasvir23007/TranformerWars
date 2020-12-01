package com.transformers.test.di

import com.transformers.test.usecases.*
import org.koin.dsl.module

val UseCaseDependency = module {

    factory {
        TransformersUseCase()
    }
    factory {
        AllSparksUseCase()
    }
    factory {
        DeleteTransformerUseCase()
    }
    factory {
        UpdateTransformerUseCase()
    }
    factory {
        CreateTransformerUseCase()
    }
    factory {
        TransformerByIdUseCase()
    }
}