package com.transformers.test.di

import com.transformers.test.repository.Repository
import org.koin.dsl.module

val RepoDependency = module {

    factory {
        Repository()
    }

}