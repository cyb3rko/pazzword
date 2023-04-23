/*
 * Copyright (c) 2023 Cyb3rKo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyb3rko.pazzword

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.security.SecureRandom

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
    val clip = ClipData.newPlainText(label, text).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.extras = PersistableBundle().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                    } else {
                        putBoolean("android.content.extra.IS_SENSITIVE", true)
                    }
                }
            }
        }
    }
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

internal fun generatorPassword(passwordType: PasswordTypes, length: Int): String {
    val stringBuilder = StringBuilder()

    val alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val alphaNumeric = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    val alphaNumericSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()"
    val numeric = "1234567890"
    val alphaSpace = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    val characters = when (passwordType) {
        PasswordTypes.ALPHA -> alpha
        PasswordTypes.ALPHANUMERIC -> alphaNumeric
        PasswordTypes.ALPHANUMERICSYMBOL -> alphaNumericSymbols
        PasswordTypes.NUMERIC -> numeric
        PasswordTypes.ALPHASPACE -> alphaSpace
    }

    val characterLength = characters.length

    // Obtain a strong SecureRandom implementation
    val secureRandom = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        SecureRandom.getInstanceStrong()
    } else {
        SecureRandom()
    }

    for ( i in 0..length) {
        val index = secureRandom.nextInt(characterLength)
        stringBuilder.append(characters[index])
    }

    return stringBuilder.toString()
}

enum class PasswordTypes {
    ALPHA,
    ALPHANUMERIC,
    ALPHANUMERICSYMBOL,
    NUMERIC,
    ALPHASPACE
}