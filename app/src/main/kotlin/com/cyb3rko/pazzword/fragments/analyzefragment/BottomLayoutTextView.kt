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

package com.cyb3rko.pazzword.fragments.analyzefragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.cyb3rko.pazzword.R

@SuppressLint("AppCompatCustomView", "ViewConstructor")
class BottomLayoutTextView(
    context: Context,
    contentEntry: Boolean,
    useMargins: Boolean,
    message: String
) : TextView(context) {

    init {
        val newLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        text = message
        if (contentEntry) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.customEntry))
            if (useMargins) newLayoutParams.setMargins(0, 4, 0, 0)
        } else {
            setTextColor(ResourcesCompat.getColor(resources, R.color.textColorSecondary, context.theme))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.customTitle))
            if (useMargins) newLayoutParams.setMargins(0, 20, 0, 0)
        }
        layoutParams = newLayoutParams
    }
}
