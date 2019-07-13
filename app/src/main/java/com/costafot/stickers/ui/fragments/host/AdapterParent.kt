package com.costafot.stickers.ui.fragments.host

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.costafot.stickers.R
import com.costafot.stickers.contentprovider.model.StickerPack
import kotlinx.android.synthetic.main.row_parent.view.*

class AdapterParent(private val callback: Callback) :
    ListAdapter<StickerPack, AdapterParent.ItemViewHolder>(DiffCallbackStickerPack()) {

    interface Callback {
        fun onAddButtonClicked(identifier: String, name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_parent, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), callback)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(stickerPack: StickerPack, callback: Callback) = with(itemView) {

            itemView.textView_stickerpack_name.text = stickerPack.name
            setupAdapter(itemView.recyclerView_child, stickerPack)

            when (stickerPack.isWhitelisted) {
                true -> {
                    itemView.button_add.text = "ADDED"
                }
                false -> {
                    itemView.button_add.text = "NOT"
                    itemView.button_add.setOnClickListener {
                        callback.onAddButtonClicked(stickerPack.identifier!!, stickerPack.name!!)
                    }
                }
            }
        }

        private fun setupAdapter(
            recyclerView: RecyclerView,
            stickerPack: StickerPack
        ) {
            val adapterChild = AdapterChild(stickerPack.identifier)
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapterChild

            adapterChild.submitList(stickerPack.stickers)
        }
    }
}

class DiffCallbackStickerPack : DiffUtil.ItemCallback<StickerPack>() {
    override fun areItemsTheSame(oldItem: StickerPack, newItem: StickerPack): Boolean {
        return oldItem.identifier == newItem.identifier
    }

    override fun areContentsTheSame(oldItem: StickerPack, newItem: StickerPack): Boolean {
        return oldItem.isWhitelisted == newItem.isWhitelisted
    }
}