package com.costafot.stickers.toaster

sealed class ToastMessage(
    open val resourceId: Int = -1,
    open val message: String? = null
) {

    data class Error(
        override val resourceId: Int = -1,
        override val message: String? = null
    ) : ToastMessage()

    data class Success(
        override val resourceId: Int = -1,
        override val message: String? = null
    ) : ToastMessage()

    data class Info(
        override val resourceId: Int = -1,
        override val message: String? = null
    ) : ToastMessage()

    fun hasResource(): Boolean {
        return resourceId != -1 && message == null
    }
}
