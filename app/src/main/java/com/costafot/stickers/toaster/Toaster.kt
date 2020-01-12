package com.costafot.stickers.toaster

import android.content.Context
import es.dmoral.toasty.Toasty

object Toaster {

    fun show(context: Context, toastMessage: ToastMessage) {
        toastMessage.resolve(
            ifResource = { resourceId: Int ->
                when (toastMessage) {
                    is ToastMessage.Error -> Toasty.error(context, context.getString(resourceId)).show()
                    is ToastMessage.Success -> Toasty.success(context, context.getString(resourceId)).show()
                    is ToastMessage.Info -> Toasty.info(context, context.getString(resourceId)).show()
                }
            },
            ifString = { message: String ->
                when (toastMessage) {
                    is ToastMessage.Error -> Toasty.error(context, message).show()
                    is ToastMessage.Success -> Toasty.success(context, message).show()
                    is ToastMessage.Info -> Toasty.info(context, message).show()
                }
            }
        )
    }
}
