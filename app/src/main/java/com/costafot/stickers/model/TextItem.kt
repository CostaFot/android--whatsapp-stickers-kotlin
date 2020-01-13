package com.costafot.stickers.model

import com.costafot.stickers.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.test_layout.*

class TextItem(val id: Int, private val text: String) : Item() {

    override fun getLayout(): Int = R.layout.test_layout

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            textView.text = text
        }
    }
}
