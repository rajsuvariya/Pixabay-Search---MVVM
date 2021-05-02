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
package com.rajsuvariya.pixabaysearch.data.source

import com.rajsuvariya.pixabaysearch.data.model.ImagesReponseModel

class ImageRepository(
        private val imagesRemoteDataSource: ImagesDataSource
) : ImagesDataSource {

    override fun getImages(query: String, page:Int, callback: ImagesDataSource.LoadImagesCallback) {

        getImagesFromRemoteDataSource(query, page, callback)
    }

    private fun getImagesFromRemoteDataSource(query: String, page: Int, callback: ImagesDataSource.LoadImagesCallback) {
        imagesRemoteDataSource.getImages(query, page, object : ImagesDataSource.LoadImagesCallback {
            override fun onImagesLoaded(imageResponseModel: ImagesReponseModel) {
                callback.onImagesLoaded(imageResponseModel)
            }

            override fun onImagesNotAvailable(errorMessage: String) {
                callback.onImagesNotAvailable("Something went wrong. Please check back later")
            }
        })
    }

    companion object {

        private var INSTANCE: ImageRepository? = null

        @JvmStatic fun getInstance(imagesRemoteDataSource: ImagesDataSource) =
                INSTANCE ?: synchronized(ImageRepository::class.java) {
                    INSTANCE ?: ImageRepository(imagesRemoteDataSource)
                            .also { INSTANCE = it }
                }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}
