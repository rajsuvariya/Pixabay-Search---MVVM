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
package com.rajsuvariya.pixabaysearch.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajsuvariya.pixabaysearch.data.model.Image
import com.rajsuvariya.pixabaysearch.data.model.ImagesReponseModel
import com.rajsuvariya.pixabaysearch.data.source.ImageRepository
import com.rajsuvariya.pixabaysearch.data.source.ImagesDataSource
import com.rajsuvariya.pixabaysearch.util.cancelIfActive
import com.rajsuvariya.pixabaysearch.util.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class SearchViewModel(
        private val imagesRepository: ImageRepository
) : ViewModel() {

    var queryText: MutableLiveData<String> = MutableLiveData()
    private var getImagesJob: Job? = null
    private var page = 1
    private var totalItems = Int.MAX_VALUE
    private val DEFAULT_PAGE_COUNT = 20

    init {
        queryText.debounce(500).observeForever {
            if (it.length > 2) {
                page = 1
                loadImages(it)
            }
        }
    }

    private val _images = MutableLiveData<List<Image>>()
    val images: LiveData<List<Image>>
        get() = _images

    private val _resetImages: MutableLiveData<Boolean> = MutableLiveData()
    val resetImages: LiveData<Boolean>
        get() = _resetImages

    private val _scrollToTop: MutableLiveData<Boolean> = MutableLiveData()
    val scrollToTop: LiveData<Boolean>
        get() = _scrollToTop


    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _snackbarText = MutableLiveData<String>()
    val snackbarMessage: LiveData<String>
        get() = _snackbarText

    fun loadNextPage(totalItemCount: Int) {
        if (queryText.value!!.length > 2 && totalItems >= totalItemCount + DEFAULT_PAGE_COUNT) {
            loadImages(queryText.value!!)
        }
    }

    private fun loadImages(query: String) {
        _dataLoading.value = true

        getImagesJob?.cancelIfActive()
        getImagesJob = viewModelScope.launch {
            imagesRepository.getImages(query, page, object : ImagesDataSource.LoadImagesCallback {
                override fun onImagesLoaded(imageResponseModel: ImagesReponseModel) {
                    _dataLoading.value = false
                    if (imageResponseModel.hits.isEmpty() && page == 1) {
                        _resetImages.value = true
                    } else {
                        if (page == 1) {
                            _resetImages.value = true
                            _scrollToTop.value = true
                        }
                        totalItems = imageResponseModel.totalHits
                        page++

                        val itemsValue = ArrayList(imageResponseModel.hits)
                        _images.value = itemsValue
                    }
                }

                override fun onImagesNotAvailable(errorMessage: String) {
                    _dataLoading.value = false
                    _snackbarText.value = errorMessage
                }
            })
        }
    }
}
