package com.transformers.test.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {
    single { provideSettingsPreferences(androidContext()) }
}

private const val PREFERENCES_FILE_KEY = "com.ct.codetest.settings_preferences"

private fun provideSettingsPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)