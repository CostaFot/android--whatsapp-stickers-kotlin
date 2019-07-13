package com.costafot.stickers.ui.fragments.host

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.costafot.stickers.R
import com.costafot.stickers.contentprovider.model.Sticker
import kotlinx.android.synthetic.main.row_child.view.*

class AdapterChild(val identifier: String?) :
    ListAdapter<Sticker, AdapterChild.ItemViewHolder>(DiffCallbackSticker()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_child, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), identifier)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(sticker: Sticker, identifier: String?) = with(itemView) {
            val path = "file:///android_asset/$identifier/${sticker.imageFileName}"
            val ff = Uri.parse(path)
            Glide.with(itemView.context)
                .load(ff)
                .placeholder(R.drawable.ic_check_white_48dp)
                .error(R.drawable.ic_error_outline_white_48dp)
                .into(itemView.imageView_sticker)
        }
    }
}

class DiffCallbackSticker : DiffUtil.ItemCallback<Sticker>() {
    override fun areItemsTheSame(oldItem: Sticker, newItem: Sticker): Boolean {
        return oldItem.imageFileName == newItem.imageFileName
    }

    override fun areContentsTheSame(oldItem: Sticker, newItem: Sticker): Boolean {
        return oldItem.imageFileName == newItem.imageFileName
    }
}