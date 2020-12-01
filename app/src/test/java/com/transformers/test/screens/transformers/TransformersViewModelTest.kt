package com.transformers.test.screens.transformers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.transformers.test.base.BaseUTTest
import com.transformers.test.di.configureTestAppComponent
import com.transformers.test.models.transformers.AllTransformers
import com.transformers.test.platform.LiveDataWrapper
import com.transformers.test.usecases.CreateTransformerUseCase
import com.transformers.test.usecases.DeleteTransformerUseCase
import com.transformers.test.usecases.TransformersUseCase
import com.transformers.test.usecases.UpdateTransformerUseCase
import com.transformers.test.viewmodels.TransformersViewModel
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

@RunWith(JUnit4::class)
class TransformersViewModelTest: BaseUTTest(){

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var mTransformersViewModel: TransformersViewModel

    val mDispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var mTransformersUseCase: TransformersUseCase
    @MockK
    lateinit var mCreateTransformerUseCase: CreateTransformerUseCase
    @MockK
    lateinit var mUpdateTransformerUseCase: UpdateTransformerUseCase
    @MockK
    lateinit var mDeleteTransformerUseCase: DeleteTransformerUseCase

    @Before
    fun start(){
        super.setUp()
        //Used for initiation of Mockk
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin{ modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_transformers_view_model_data_populates_expected_value(){

        mTransformersViewModel =
            TransformersViewModel(
                mDispatcher,
                mDispatcher,
                mTransformersUseCase,
                mCreateTransformerUseCase,
                mUpdateTransformerUseCase,
                mDeleteTransformerUseCase
            )
        val sampleResponse = getJson("success_resp_list.json")
        val jsonObj = Gson().fromJson(sampleResponse, AllTransformers::class.java)

        coEvery { mTransformersUseCase.processTransformersUseCase() } returns jsonObj
        mTransformersViewModel.mTransformersResponse.observeForever {  }

        mTransformersViewModel.requestTransformersData()

        assert(mTransformersViewModel.mTransformersResponse.value != null)
        assert(mTransformersViewModel.mTransformersResponse.value!!.responseStatus == LiveDataWrapper.ResponseStatus.SUCCESS)
        val testResult = mTransformersViewModel.mTransformersResponse.value as LiveDataWrapper<AllTransformers>
        assertEquals(testResult.response?.transformers?.size, 7)
    }
}