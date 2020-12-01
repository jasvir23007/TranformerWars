package com.transformers.test.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.transformers.test.base.BaseUTTest
import com.transformers.test.di.configureTestAppComponent
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
class RepositoryTest : BaseUTTest(){

    //Target
    private lateinit var mRepo: Repository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val mCount = 7

    @Before
    fun start(){
        super.setUp()

        startKoin{
            modules(configureTestAppComponent(getMockWebServerUrl()))
        }
    }

    @Test
    fun test_transformers_repo_retrieves_expected_data() =  runBlocking<Unit>{

        mockNetworkResponseWithFileContent("success_resp_list.json", HttpURLConnection.HTTP_OK)
        mRepo = Repository()

        val dataReceived = mRepo.getTransformersData()

        assertNotNull(dataReceived)
        assertEquals(dataReceived.transformers.size, mCount)
    }
}