package com.cyb3rko.pazzword.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cyb3rko.pazzword.R
import com.cyb3rko.pazzword.openURL
import com.google.android.material.card.MaterialCardView

class AboutAnimationsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myContext = requireContext()
        val information = listOf(
            Triple(
                "Password Unlock",
                "JA Studio",
                "https://lottiefiles.com/67204-password-unlock"
            ),
            Triple(
                "Lock CPU (Cyber Security)",
                "Ision Industries",
                "https://lottiefiles.com/34705-lock-cpu-cyber-security"
            ),
            Triple(
                "Unlocking",
                "LottieFiles",
                "https://lottiefiles.com/13164-unlocking"
            ),
            Triple(
                "robot process automation",
                "Scott A",
                "https://lottiefiles.com/23491-robot-process-automation"
            )
        )
        val view = ScrollView(myContext)
        val linearLayout = LinearLayout(myContext)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPaddingRelative(50, 50, 50, 0)
        information.forEach {
            val textView = TextView(myContext)
            textView.textSize = 18f
            textView.setPaddingRelative(50, 50, 50, 50)
            val text = getString(R.string.about_animations_description, it.first, it.second)
            val spannableString = SpannableString(text)
            val clickableSpan = object: ClickableSpan() {
                override fun onClick(widget: View) {
                    openURL(it.third)
                }
            }
            spannableString.setSpan(
                clickableSpan,
                0,
                it.first.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            textView.text = spannableString
            textView.movementMethod = LinkMovementMethod.getInstance()
            val card = MaterialCardView(myContext)
            card.setMargins(0, 0, 0, 20)
            card.addView(textView)
            linearLayout.addView(card)
        }
        view.addView(linearLayout)
        return view
    }

    private fun View.setMargins(start: Int, top: Int, end: Int, bottom: Int) {
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.MATCH_PARENT,
            ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(start, top, end, bottom)
        this.layoutParams = layoutParams
        this.requestLayout()
    }
}
