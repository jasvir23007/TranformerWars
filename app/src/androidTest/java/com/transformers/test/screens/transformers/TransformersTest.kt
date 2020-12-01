package com.transformers.test.screens.transformers

import android.os.SystemClock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.transformers.test.R
import com.transformers.test.base.BaseUITest
import com.transformers.test.di.generateTestAppComponent
import com.transformers.test.helpers.recyclerItemAtPosition
import com.transformers.test.screens.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4::class)
class TransformersTest : BaseUITest(){

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    val mNameTestOne = "soundtrack"
    val mNameTestTwo = "hubcap"

    @Before
    fun start(){
        super.setUp()
        loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
    }

    @Test
    fun test_recyclerview_elements_for_expected_response() {
        mActivityTestRule.launchActivity(null)

        mockNetworkResponseWithFileContent("success_resp_list.json", HttpURLConnection.HTTP_OK)

        //Wait for MockWebServer to get back with response
        SystemClock.sleep(1000)

        //Check if item at 0th position is having 0th element in json
        onView(withId(R.id.transformersListRecyclerView))
            .check(
                matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(withText(mNameTestOne))
                    )
                ))

        //Scroll to last index in json
        onView(withId(R.id.transformersListRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<TransformersAdapter.TransformersFragViewHolder>(2))

        //Check if item at 2nf position is having 2nd element in json
        onView(withId(R.id.transformersListRecyclerView))
            .check(matches(recyclerItemAtPosition(2, ViewMatchers.hasDescendant(withText(mNameTestTwo)))))


    }
}