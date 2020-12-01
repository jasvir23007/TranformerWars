package com.transformers.test.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.transformers.test.platform.LiveDataWrapper
import com.transformers.test.usecases.AllSparksUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class LoginViewModel(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val allSparksUseCase: AllSparksUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel(), KoinComponent {

    var mAllSparksResponse = MutableLiveData<LiveDataWrapper<String>>()
    val mLoadingLiveData = MutableLiveData<Boolean>()
    private val job = SupervisorJob()
    private val mUiScope = CoroutineScope(mainDispatcher + job)
    private val mIoScope = CoroutineScope(ioDispatcher + job)

    fun requestAllSparksData() {
        mUiScope.launch {
            mAllSparksResponse.value = LiveDataWrapper.loading()
            setLoadingVisibility(true)
            try {
                val data = mIoScope.async {
                    return@async allSparksUseCase.processAllSparksUseCase()
                }.await()
                mAllSparksResponse.value = LiveDataWrapper.success(data)
                setLoadingVisibility(false)
            } catch (e: Exception) {
                setLoadingVisibility(false)
                e.printStackTrace()
                mAllSparksResponse.value = LiveDataWrapper.error(e)
            }
        }

    }

    private fun setLoadingVisibility(visible: Boolean) {
        mLoadingLiveData.postValue(visible)
    }

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }
}