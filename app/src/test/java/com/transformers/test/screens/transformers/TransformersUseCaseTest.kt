package com.transformers.test.screens.transformers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.transformers.test.base.BaseUTTest
import com.transformers.test.di.configureTestAppComponent
import com.transformers.test.usecases.TransformersUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class TransformersUseCaseTest : BaseUTTest() {

    //Target
    private lateinit var transformersUseCase: TransformersUseCase


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val mCount = 7

    @Before
    fun start() {
        super.setUp()
        startKoin {
            modules(configureTestAppComponent(getMockWebServerUrl()))
        }
    }

    @Test
    fun test_transformers_use_case_returns_expected_value() = runBlocking {

        mockNetworkResponseWithFileContent("success_resp_list.json", HttpURLConnection.HTTP_OK)
        transformersUseCase = TransformersUseCase()

        val dataReceived = transformersUseCase.processTransformersUseCase()

        assertNotNull(dataReceived)
        assertEquals(dataReceived.transformers.size, mCount)
    }

}