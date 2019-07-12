package com.feelsokman.stickers.ui.fragments.host

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.feelsokman.stickers.R
import com.feelsokman.stickers.contentprovider.model.StickerPack

class AdapterMaster() :
    ListAdapter<StickerPack, AdapterMaster.ItemViewHolder>(DiffCallbackStickerPack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(any: StickerPack) = with(itemView) {
        }
    }
}

class DiffCallbackStickerPack : DiffUtil.ItemCallback<StickerPack>() {
    override fun areItemsTheSame(oldItem: StickerPack, newItem: StickerPack): Boolean {
        return oldItem.identifier == newItem.identifier
    }

    override fun areContentsTheSame(oldItem: StickerPack, newItem: StickerPack): Boolean {
        return oldItem.identifier == newItem.identifier
    }
}