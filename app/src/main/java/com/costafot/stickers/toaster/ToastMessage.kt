package com.costafot.stickers.toaster

sealed class ToastMessage(
    open val resource: Resource? = null,
    open val message: Message = Message("")
) {

    data class Error(
        override val resource: Resource? = null,
        override val message: Message = Message("")
    ) : ToastMessage()

    data class Success(
        override val resource: Resource? = null,
        override val message: Message = Message("")
    ) : ToastMessage()

    data class Info(
        override val resource: Resource? = null,
        override val message: Message = Message("")
    ) : ToastMessage()
}

data class Resource(val resourceId: Int)
data class Message(val msg: String)

fun ToastMessage.resolve(
    ifResource: (resourceId: Int) -> Unit,
    ifString: (message: String) -> Unit
) {
    resource?.let {
        ifResource(it.resourceId)
    } ?: run {
        ifString(message.msg)
    }
}
