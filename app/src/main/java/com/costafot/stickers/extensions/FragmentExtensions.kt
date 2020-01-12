package com.costafot.stickers.extensions

import androidx.fragment.app.Fragment
import com.costafot.stickers.toaster.ToastMessage
import com.squareup.otto.Bus

fun Fragment.registerBus(bus: Bus) {
    bus.register(this)
}

fun Fragment.unregisterBus(bus: Bus) {
    bus.unregister(this)
}

fun Fragment.toast(toastMessage: ToastMessage) {
    requireContext().toast(toastMessage)
}
