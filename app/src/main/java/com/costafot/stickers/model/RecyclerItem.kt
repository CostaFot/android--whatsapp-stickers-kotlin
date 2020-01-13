package com.costafot.stickers.model

import com.costafot.stickers.R
import com.costafot.stickers.contentprovider.model.StickerPack
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.row_parent.*

class RecyclerItem(private val stickerPack: StickerPack) : Item() {

    override fun getLayout(): Int = R.layout.row_parent

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            textView_stickerpack_name.text = stickerPack.name
        }
    }
}
