package com.transformers.test.network.transformers

import com.transformers.test.models.transformers.AllTransformers
import com.transformers.test.models.transformers.Transformer
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET("allspark")
    suspend fun getAllSpark(): String

    @GET("transformers")
    suspend fun getTransformers(@Header("Authorization") token: String): AllTransformers

    @POST("transformers")
    suspend fun createTransformer(@Header("Authorization") token: String, @Body transformer: Transformer): Transformer

    @PUT("transformers")
    suspend fun updateTransformer(@Header("Authorization") token: String, @Body transformer: Transformer): Transformer

    @GET("transformers/{id}")
    suspend fun getTransformerById(@Header("Authorization") token: String, @Path("id") id: String): Transformer

    @DELETE("transformers/{id}")
    suspend fun deleteTransformers(@Header("Authorization") token: String, @Path("id") id: String): Response<Unit>

}