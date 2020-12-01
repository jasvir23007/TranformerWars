package com.transformers.test.platform

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.transformers.test.usecases.*
import com.transformers.test.viewmodels.LoginViewModel
import com.transformers.test.viewmodels.TransformersViewModel
import kotlinx.coroutines.CoroutineDispatcher

class BaseViewModelFactory constructor(
    private val mainDispather: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
    private val transformersUseCase: TransformersUseCase,
    private val allSparksUseCase: AllSparksUseCase,
    private val deleteTransformerUseCase: DeleteTransformerUseCase,
    private val createTransformerUseCase: CreateTransformerUseCase,
    private val updateTransformerUseCase: UpdateTransformerUseCase,
    private val transformerByIdUseCase: TransformerByIdUseCase,
    private val sharedPreferences: SharedPreferences
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TransformersViewModel::class.java) -> {
                TransformersViewModel(
                    mainDispather,
                    ioDispatcher,
                    transformersUseCase,
                    createTransformerUseCase,
                    updateTransformerUseCase,
                    deleteTransformerUseCase
                ) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(
                    mainDispather,
                    ioDispatcher,
                    allSparksUseCase,
                    sharedPreferences
                ) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not configured") as Throwable
            }
        }
    }
}