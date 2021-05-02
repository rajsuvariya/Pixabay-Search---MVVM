package com.rajsuvariya.pixabaysearch.data.source.remote

import com.rajsuvariya.pixabaysearch.data.model.ImagesReponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("api")
    fun getImages(@Query("key") key: String,
                  @Query("q") q: String,
                  @Query("page") page: Int): Call<ImagesReponseModel>
}