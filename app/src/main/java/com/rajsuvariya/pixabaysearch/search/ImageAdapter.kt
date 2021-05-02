package com.rajsuvariya.pixabaysearch.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajsuvariya.pixabaysearch.R
import com.rajsuvariya.pixabaysearch.data.model.Image

class ImageAdapter(val images: MutableList<Image>, val imageClickListener: ImageClickListener) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    interface ImageClickListener {
        fun onImageClicked(image: Image)
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        init {
            itemView.setOnClickListener {
                imageClickListener.onImageClicked(images[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_imagesearch, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.ivImage.context)
                .load(images[position].previewURL)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(holder.ivImage)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun addImages(newImages: List<Image>) {
        val initialCount = images.size
        images.addAll(newImages)
        notifyItemRangeInserted(initialCount, newImages.size)
    }

    fun removeAllImages() {
        images.clear()
        notifyDataSetChanged()
    }
}