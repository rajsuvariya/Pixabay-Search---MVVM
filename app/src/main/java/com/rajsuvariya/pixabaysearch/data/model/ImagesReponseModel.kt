package com.rajsuvariya.pixabaysearch.data.model

import com.google.gson.annotations.SerializedName

data class ImagesReponseModel(
        @SerializedName("total") val total: Int,
        @SerializedName("totalHits") val totalHits: Int,
        @SerializedName("hits") val hits: List<Image>
)