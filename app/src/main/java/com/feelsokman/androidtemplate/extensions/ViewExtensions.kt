package com.feelsokman.androidtemplate.extensions

import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.view.ViewCompat
import com.squareup.otto.Bus

/**
 * Button enabling/disabling modifiers
 */

fun Button.disableButton() {
    isEnabled = false
    alpha = 0.7f
}

fun Button.enableButton() {
    isEnabled = true
    alpha = 1.0f
}

/**
 * Adds TextWatcher to the EditText
 */
fun EditText.onTextChanged(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

// Get screen height
fun Activity.getScreenHeight(): Int {
    val size = Point()
    windowManager.defaultDisplay.getSize(size)
    return size.y
}

/**
 * Helps to set clickable part in text.
 *
 * Don't forget to set android:textColorLink="@color/link" (click selector) and
 * android:textColorHighlight="@color/window_background" (background color while clicks)
 * in the TextView where you will use this.
 */
fun SpannableString.withClickableSpan(clickablePart: String, onClickListener: () -> Unit): SpannableString {
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View?) = onClickListener.invoke()
    }
    val clickablePartStart = indexOf(clickablePart)
    setSpan(clickableSpan, clickablePartStart, clickablePartStart + clickablePart.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

// Adds flags to make window fullscreen
fun Activity.setFullscreenLayoutFlags() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

// Adds window insets to the view while entire activity is fullscreen.
fun View.applyWindowInsets(applyTopInset: Boolean = true, applyOtherInsets: Boolean = true) {
    if (applyTopInset || applyOtherInsets) {
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            // Set padding for needed insets
            view.setPadding(
                if (applyOtherInsets) insets.systemWindowInsetLeft else view.paddingLeft,
                if (applyTopInset) insets.systemWindowInsetTop else view.paddingTop,
                if (applyOtherInsets) insets.systemWindowInsetRight else view.paddingRight,
                if (applyOtherInsets) insets.systemWindowInsetBottom else view.paddingBottom
            )

            // Return without consumed insets
            insets.replaceSystemWindowInsets(
                if (applyOtherInsets) 0 else insets.systemWindowInsetLeft,
                if (applyTopInset) 0 else insets.systemWindowInsetTop,
                if (applyOtherInsets) 0 else insets.systemWindowInsetRight,
                if (applyOtherInsets) 0 else insets.systemWindowInsetBottom
            )
        }
    } else {
        // Listener is not needed
        ViewCompat.setOnApplyWindowInsetsListener(this, null)
    }
}

fun View.registerBus(bus: Bus) {
    bus.register(this)
}

fun View.unregisterBus(bus: Bus) {
    bus.unregister(this)
}
