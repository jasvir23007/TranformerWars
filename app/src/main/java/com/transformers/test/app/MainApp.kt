package com.transformers.test.app

import androidx.multidex.MultiDexApplication
import com.transformers.test.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class MainApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initiateKoin()
    }

    private fun initiateKoin() {
        startKoin{
            androidLogger()
            androidContext(this@MainApp)
            androidFileProperties()
            modules(provideDependency())
        }
    }

    open fun provideDependency() = appComponent
}