/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rajsuvariya.pixabaysearch.data.source.remote

import com.rajsuvariya.pixabaysearch.BuildConfig
import com.rajsuvariya.pixabaysearch.data.model.ImagesReponseModel
import com.rajsuvariya.pixabaysearch.data.source.ImagesDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Implementation of the data source that adds a latency simulating network.
 */
object ImagesRemoteDataSource : ImagesDataSource {

    private const val API_KEY = BuildConfig.PIXABAY_KEY
    private const val BASE_URL = BuildConfig.BASE_URL

    private lateinit var apiService: APIService
    private val okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit

    init {
        okHttpClient = createOkHttpClient()
        createServices(okHttpClient)
    }

    private fun createServices(okHttpClient: OkHttpClient) {
        apiService = createAPIService(okHttpClient)
    }

    private fun createAPIService(client: OkHttpClient): APIService {
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        return retrofit.create(APIService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    override fun getImages(query: String, page: Int, callback: ImagesDataSource.LoadImagesCallback) {
        apiService.getImages(API_KEY, query, page).enqueue(object : Callback<ImagesReponseModel> {
            override fun onResponse(call: Call<ImagesReponseModel>, response: Response<ImagesReponseModel>) {
                response.body()?.let {
                    callback.onImagesLoaded(it)
                } ?: run {
                    callback.onImagesNotAvailable("Something went wrong. Response code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ImagesReponseModel>, t: Throwable) {
                callback.onImagesNotAvailable("Something went wrong. " + t.localizedMessage)
            }
        })
    }
}
