package com.cyb3rko.pazzword.fragments

import android.content.Intent
import android.net.Uri
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

class AboutAnimationsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val myContext = requireContext()
        val information = listOf(
            Triple("Password Unlock", "JA Studio", "https://lottiefiles.com/67204-password-unlock"),
            Triple("Lock CPU (Cyber Security)", "Ision Industries", "https://lottiefiles.com/34705-lock-cpu-cyber-security"),
            Triple("Unlocking", "LottieFiles", "https://lottiefiles.com/13164-unlocking")
        )
        val view = ScrollView(myContext)
        val linearLayout = LinearLayout(myContext)
        linearLayout.orientation = LinearLayout.VERTICAL
        information.forEach {
            val textView = TextView(myContext)
            textView.textSize = 18f
            textView.setPaddingRelative(40, 50, 40, 0)
            val text = getString(R.string.about_animations_description, it.first, it.second)
            val spannableString = SpannableString(text)
            val clickableSpan = object: ClickableSpan() {
                override fun onClick(widget: View) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.third)))
                }
            }
            spannableString.setSpan(clickableSpan, 0, it.first.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textView.text = spannableString
            textView.movementMethod = LinkMovementMethod.getInstance()
            linearLayout.addView(textView)
        }
        view.addView(linearLayout)
        return view
    }
}
