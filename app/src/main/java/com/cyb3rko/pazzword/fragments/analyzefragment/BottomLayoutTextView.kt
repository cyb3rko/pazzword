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
            setTextColor(ResourcesCompat.getColor(resources, R.color.text_color_secondary, context.theme))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.customTitle))
            if (useMargins) newLayoutParams.setMargins(0, 20, 0, 0)
        }
        layoutParams = newLayoutParams
    }
}
