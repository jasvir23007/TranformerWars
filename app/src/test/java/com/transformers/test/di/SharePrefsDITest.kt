package com.transformers.test.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.dsl.module
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

val preferencesModule = module {
    single { provideSettingsPreferences() }
}

private const val PREFERENCES_FILE_KEY = "com.ct.codetest.settings_preferences"

private fun provideSettingsPreferences(): SharedPreferences {
    val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
    val context = Mockito.mock(Context::class.java)
    Mockito.`when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs)

    return context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
}
