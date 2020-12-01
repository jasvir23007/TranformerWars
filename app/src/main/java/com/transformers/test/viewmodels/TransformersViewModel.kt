package com.transformers.test.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.transformers.test.models.transformers.AllTransformers
import com.transformers.test.models.transformers.Transformer
import com.transformers.test.platform.LiveDataWrapper
import com.transformers.test.usecases.CreateTransformerUseCase
import com.transformers.test.usecases.DeleteTransformerUseCase
import com.transformers.test.usecases.TransformersUseCase
import com.transformers.test.usecases.UpdateTransformerUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class TransformersViewModel(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val transformersUseCase: TransformersUseCase,
    private val createTransformerUseCase: CreateTransformerUseCase,
    private val updateTransformerUseCase: UpdateTransformerUseCase,
    private val deleteTransformerUseCase: DeleteTransformerUseCase
) : ViewModel(), KoinComponent {

    var mTransformersResponse = MutableLiveData<LiveDataWrapper<AllTransformers>>()
    var mCreateTransformerResponse = MutableLiveData<LiveDataWrapper<Transformer>>()
    var mUpdateTransformerResponse = MutableLiveData<LiveDataWrapper<Transformer>>()
    var mDeleteTransformerResponse = MutableLiveData<LiveDataWrapper<Boolean>>()
    val mLoadingLiveData = MutableLiveData<Boolean>()
    private val job = SupervisorJob()
    private val mUiScope = CoroutineScope(mainDispatcher + job)
    private val mIoScope = CoroutineScope(ioDispatcher + job)

    var transformer: Transformer = Transformer()
    var transformers: ArrayList<Transformer> = arrayListOf()

    fun requestTransformersData() {
        mUiScope.launch {
            mTransformersResponse.value = LiveDataWrapper.loading()
            setLoadingVisibility(true)
            try {
                val data = mIoScope.async {
                    return@async transformersUseCase.processTransformersUseCase()
                }.await()
                mTransformersResponse.value = LiveDataWrapper.success(data)
                transformers = data.transformers as ArrayList<Transformer>
                setLoadingVisibility(false)
            } catch (e: Exception) {
                setLoadingVisibility(false)
                mTransformersResponse.value = LiveDataWrapper.error(e)
            }
        }
    }

    fun deleteTransformer(id: String) {
        mUiScope.launch {
            mDeleteTransformerResponse.value = LiveDataWrapper.loading()
            setLoadingVisibility(true)
            try {
                val data = mIoScope.async {
                    return@async deleteTransformerUseCase.processdeleteTransformerUseCase(id)
                }.await()
                mDeleteTransformerResponse.value = LiveDataWrapper.success(data)
                setLoadingVisibility(false)
            } catch (e: Exception) {
                setLoadingVisibility(false)
                mDeleteTransformerResponse.value = LiveDataWrapper.error(e)
            }
        }
    }

    fun createTransformer() {
        mUiScope.launch {
            mCreateTransformerResponse.value = LiveDataWrapper.loading()
            setLoadingVisibility(true)
            try {
                val check = checkTransformerData()
                if (check.isNotEmpty()) {
                    mUpdateTransformerResponse.value = LiveDataWrapper.error(Throwable(check))
                    setLoadingVisibility(false)
                    return@launch
                }
                val data = mIoScope.async {
                    return@async createTransformerUseCase.processTransformersUseCase(transformer)
                }.await()
                mCreateTransformerResponse.value = LiveDataWrapper.success(data)
                setLoadingVisibility(false)
            } catch (e: Exception) {
                setLoadingVisibility(false)
                mCreateTransformerResponse.value = LiveDataWrapper.error(e)
            }
        }
    }

    fun updateTransformer() {
        mUiScope.launch {
            mUpdateTransformerResponse.value = LiveDataWrapper.loading()
            setLoadingVisibility(true)
            try {
                val check = checkTransformerData()
                if (check.isNotEmpty()) {
                    mUpdateTransformerResponse.value = LiveDataWrapper.error(Throwable(check))
                    setLoadingVisibility(false)
                    return@launch
                }
                val data = mIoScope.async {
                    return@async updateTransformerUseCase.processTransformersUseCase(transformer)
                }.await()
                mUpdateTransformerResponse.value = LiveDataWrapper.success(data)
                setLoadingVisibility(false)
            } catch (e: Exception) {
                setLoadingVisibility(false)
                mUpdateTransformerResponse.value = LiveDataWrapper.error(e)
            }
        }
    }

    private fun checkTransformerData(): String {
        return if (!(transformer.team == "A" || transformer.team == "D")) {
            "Team should be A (autobots) or D (descepticons)"
        } else if (transformer.rank !in 1..10 &&
            transformer.strength !in 1..10 &&
            transformer.courage !in 1..10 &&
            transformer.endurance !in 1..10 &&
            transformer.firepower !in 1..10 &&
            transformer.intelligence !in 1..10 &&
            transformer.speed !in 1..10 &&
            transformer.skill !in 1..10) {
            "All powers should be between 1 and 10"
        } else ""
    }

    private fun setLoadingVisibility(visible: Boolean) {
        mLoadingLiveData.postValue(visible)
    }

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }
}