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
package com.rajsuvariya.pixabaysearch.imageDetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rajsuvariya.pixabaysearch.R
import com.rajsuvariya.pixabaysearch.data.model.Image
import com.rajsuvariya.pixabaysearch.databinding.ImageDetailsActBinding
import com.rajsuvariya.pixabaysearch.util.setupActionBar


class ImageDetailsActivity : AppCompatActivity() {

    private lateinit var activityImageDetailsActBinding: ImageDetailsActBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityImageDetailsActBinding = ImageDetailsActBinding.inflate(layoutInflater)
        setContentView(activityImageDetailsActBinding.root)

        setupActionBar(R.id.toolbar) {
            setTitle(R.string.app_name)
        }

        val image = intent.getParcelableExtra<Image>("image")
        if (image == null) {
            finish()
        }

        activityImageDetailsActBinding.shimmerViewContainer.startShimmer()
        Glide.with(this)
                .load(image?.largeImageURL)
                .placeholder(R.drawable.ic_baseline_image_24)
                .addListener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Log.d("CustomTag", "onLoadFailed")
                        activityImageDetailsActBinding.shimmerViewContainer.hideShimmer()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Log.d("CustomTag", "onResourceReady")

                        activityImageDetailsActBinding.shimmerViewContainer.hideShimmer()
                        return false
                    }
                })
                .into(activityImageDetailsActBinding.ivImage)
    }
}