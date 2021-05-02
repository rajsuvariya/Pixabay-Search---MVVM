package com.rajsuvariya.pixabaysearch.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Image (
        @SerializedName("id") val id: Int,
        @SerializedName("pageURL") val pageURL: String,
        @SerializedName("type") val type: String,
        @SerializedName("tags") val tags: String,
        @SerializedName("previewURL") val previewURL: String,
        @SerializedName("previewWidth") val previewWidth: Int,
        @SerializedName("previewHeight") val previewHeight: Int,
        @SerializedName("webformatURL") val webformatURL: String,
        @SerializedName("webformatWidth") val webformatWidth: Int,
        @SerializedName("webformatHeight") val webformatHeight: Int,
        @SerializedName("largeImageURL") val largeImageURL: String,
        @SerializedName("imageWidth") val imageWidth: Int,
        @SerializedName("imageHeight") val imageHeight: Int,
        @SerializedName("imageSize") val imageSize: Int,
        @SerializedName("views") val views: Int,
        @SerializedName("downloads") val downloads: Int,
        @SerializedName("favorites") val favorites: Int,
        @SerializedName("likes") val likes: Int,
        @SerializedName("comments") val comments: Int,
        @SerializedName("user_id") val user_id: Int,
        @SerializedName("user") val user: String,
        @SerializedName("userImageURL") val userImageURL: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(pageURL)
        parcel.writeString(type)
        parcel.writeString(tags)
        parcel.writeString(previewURL)
        parcel.writeInt(previewWidth)
        parcel.writeInt(previewHeight)
        parcel.writeString(webformatURL)
        parcel.writeInt(webformatWidth)
        parcel.writeInt(webformatHeight)
        parcel.writeString(largeImageURL)
        parcel.writeInt(imageWidth)
        parcel.writeInt(imageHeight)
        parcel.writeInt(imageSize)
        parcel.writeInt(views)
        parcel.writeInt(downloads)
        parcel.writeInt(favorites)
        parcel.writeInt(likes)
        parcel.writeInt(comments)
        parcel.writeInt(user_id)
        parcel.writeString(user)
        parcel.writeString(userImageURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }

}