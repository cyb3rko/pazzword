package com.cyb3rko.pazzword

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

internal fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

internal fun Fragment.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(message, length)
}

internal fun Context.showClipboardToast(content: String) {
    showToast(getString(R.string.clipboard_info, content))
}

internal fun Fragment.showClipboardToast(content: String) {
    requireContext().showClipboardToast(content)
}

internal fun Context.storeToClipboard(label: String, text: String = label) {
    val clip = ClipData.newPlainText(label, text)
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .setPrimaryClip(clip)
}

internal fun Fragment.storeToClipboard(label: String, text: String = label) {
    requireContext().storeToClipboard(label, text)
}

internal fun Context.openURL(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

internal fun Fragment.openURL(url: String) {
    requireContext().openURL(url)
}

internal fun Fragment.hideKeyboard() {
    val activity = requireActivity()
    val imm = activity.getSystemService(
        AppCompatActivity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

internal fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}
