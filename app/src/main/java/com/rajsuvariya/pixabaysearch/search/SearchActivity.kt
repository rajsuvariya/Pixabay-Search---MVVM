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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rajsuvariya.pixabaysearch.R
import com.rajsuvariya.pixabaysearch.data.model.Image
import com.rajsuvariya.pixabaysearch.databinding.SearchActBinding
import com.rajsuvariya.pixabaysearch.imageDetails.ImageDetailsActivity
import com.rajsuvariya.pixabaysearch.util.hideKeyboard
import com.rajsuvariya.pixabaysearch.util.obtainViewModel
import com.rajsuvariya.pixabaysearch.util.setupActionBar
import com.rajsuvariya.pixabaysearch.util.showSnackbar


class SearchActivity : AppCompatActivity(), ImageAdapter.ImageClickListener {

    /**
     * Scope for improvements
     * 1. Internet check before calling API
     * 2. Proguard
     * 3. Test cases
     * 4. Haven't focused on UI much, it can be certainly improved
     */

    private lateinit var viewModel: SearchViewModel
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imagesList: MutableList<Image>
    private lateinit var activitySearchBinding: SearchActBinding
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySearchBinding = SearchActBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)

        setupActionBar(R.id.toolbar) {
            setTitle(R.string.app_name)
        }

        imagesList = mutableListOf()
        imageAdapter = ImageAdapter(imagesList, this)
        activitySearchBinding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 && !loading) { //check for scroll down
                        val visibleItemCount: Int = layoutManager?.childCount ?: 0
                        val totalItemCount: Int = layoutManager?.itemCount ?: 0

                        var firstVisibleItems: IntArray? = null
                        var pastVisibleItems: Int = 0
                        firstVisibleItems = (layoutManager as StaggeredGridLayoutManager)
                                .findFirstVisibleItemPositions(firstVisibleItems)
                        if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                            pastVisibleItems = firstVisibleItems[0]
                        }

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            viewModel.loadNextPage(totalItemCount)
                            loading = true
                        }
                    }
                }
            })
        }

        viewModel = obtainViewModel().apply {
            images.observe(this@SearchActivity, {
                loading = false
                activitySearchBinding.rvImages.visibility = View.VISIBLE
                activitySearchBinding.noImage.visibility = View.GONE
                this@SearchActivity.hideKeyboard(activitySearchBinding.tilSearchBar)
                imageAdapter.addImages(it)
            })
            resetImages.observe(this@SearchActivity, {
                Log.d("CustomTag", "resetImages.observe")
                activitySearchBinding.rvImages.visibility = View.GONE
                activitySearchBinding.noImage.visibility = View.VISIBLE
                imageAdapter.removeAllImages()
            })
            snackbarMessage.observe(this@SearchActivity, {
                activitySearchBinding.root.showSnackbar(it, Snackbar.LENGTH_LONG)
            })
            dataLoading.observe(this@SearchActivity, {
                activitySearchBinding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
            })
            scrollToTop.observe(this@SearchActivity, {
                activitySearchBinding.rvImages.smoothScrollToPosition(0)
            })
        }

        activitySearchBinding.viewmodel = viewModel
    }

    private fun obtainViewModel(): SearchViewModel = obtainViewModel(SearchViewModel::class.java)

    override fun onImageClicked(image: Image) {
        val intent = Intent(this, ImageDetailsActivity::class.java)
        intent.putExtra("image", image)
        startActivity(intent)
    }
}