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

interface ImagesDataSource {

    interface LoadImagesCallback {

        fun onImagesLoaded(imageResponseModel: ImagesReponseModel)

        fun onImagesNotAvailable(errorMessage: String)
    }

    fun getImages(query: String, page:Int, callback: LoadImagesCallback)
}