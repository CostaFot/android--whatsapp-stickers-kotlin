package com.costafot.stickers.model

data class ToastMessage(val resourceId: Int = -1, val message: String? = null) {
    fun hasResource(): Boolean = resourceId != -1 && message == null
}