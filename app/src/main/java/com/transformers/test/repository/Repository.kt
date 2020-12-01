package com.transformers.test.repository

import android.content.SharedPreferences
import com.transformers.test.models.transformers.AllTransformers
import com.transformers.test.models.transformers.Transformer
import com.transformers.test.network.transformers.APIService
import org.koin.core.KoinComponent
import org.koin.core.inject

class Repository : KoinComponent {

    private val mAPIService: APIService by inject()
    private val sharedPreferences by inject<SharedPreferences>()

    suspend fun getAllSparks(): String {
        return mAPIService.getAllSpark()
    }

    suspend fun getTransformersData(): AllTransformers {
        val token = sharedPreferences.getString("token", "")
        return mAPIService.getTransformers("Bearer $token")
    }
    suspend fun getTransformerByIdData(id: String): Transformer {
        val token = sharedPreferences.getString("token", "")
        return mAPIService.getTransformerById("Bearer $token", id)
    }

    suspend fun createTransformer(transformer: Transformer): Transformer {
        val token = sharedPreferences.getString("token", "")
        return mAPIService.createTransformer("Bearer $token", transformer)
    }

    suspend fun updateTransformer(transformer: Transformer): Transformer {
        val token = sharedPreferences.getString("token", "")
        return mAPIService.updateTransformer("Bearer $token", transformer)
    }
    suspend fun deleteTransformers(id: String): Boolean {
        val token = sharedPreferences.getString("token", "")
        val call = mAPIService.deleteTransformers("Bearer $token", id)
        return call.code() == 204
    }


}