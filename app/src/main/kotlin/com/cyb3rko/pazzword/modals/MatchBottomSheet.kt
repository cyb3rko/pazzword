package com.cyb3rko.pazzword.modals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.cyb3rko.pazzword.R
import com.cyb3rko.pazzword.databinding.BottomSheetMatchBinding
import com.cyb3rko.pazzword.fragments.analyzefragment.BottomLayoutTextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MatchBottomSheet(
    private val matchDetails: List<String>,
    private val type: String
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentActivity = context as FragmentActivity
        @SuppressLint("CheckResult", "InflateParams")
        val layout = fragmentActivity.layoutInflater.inflate(
            R.layout.bottom_sheet_match,
            null
        ) as LinearLayout
        val binding = BottomSheetMatchBinding.bind(layout)

        matchDetails.forEachIndexed { index, s ->
            val parts = s.split(": ")
            layout.addView(
                BottomLayoutTextView(
                requireContext(),
                false,
                index != 0,
                parts[0]
            )
            )
            layout.addView(
                BottomLayoutTextView(
                requireContext(),
                true,
                false,
                parts[1]
            )
            )
        }

        binding.title.text = type
        return layout
    }

    companion object {
        const val TAG = "Result Bottom Sheet"
    }
}
