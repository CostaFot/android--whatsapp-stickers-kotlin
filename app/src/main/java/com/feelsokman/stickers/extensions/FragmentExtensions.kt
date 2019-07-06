package com.feelsokman.stickers.extensions

import androidx.fragment.app.Fragment
import com.squareup.otto.Bus

fun Fragment.registerBus(bus: Bus) {
    bus.register(this)
}

fun Fragment.unregisterBus(bus: Bus) {
    bus.unregister(this)
}