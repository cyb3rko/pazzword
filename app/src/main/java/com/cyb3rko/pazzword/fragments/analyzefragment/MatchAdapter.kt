package com.cyb3rko.pazzword.fragments.analyzefragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.cyb3rko.pazzword.R
import com.google.android.material.divider.MaterialDivider
import me.gosimple.nbvcxz.matching.match.Match

class MatchAdapter(
    private val context: Context,
    private val recycler: RecyclerView,
    private val matches: List<Match>
) : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_view)
        val divider: MaterialDivider = itemView.findViewById(R.id.divider)
    }

    @SuppressLint("CheckResult")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.match_item, parent, false)
        v.setOnClickListener {
            val position = recycler.getChildLayoutPosition(it)
            val match = matches[position]
            val matchDetails = match.details.lines().toMutableList()
            val type = matchDetails[0].split(": ")[1]
            matchDetails.removeAt(0)

            val bottomLayout = LinearLayout(context)
            bottomLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            bottomLayout.orientation = LinearLayout.VERTICAL
            matchDetails.forEachIndexed { index, s ->
                val parts = s.split(": ")
                bottomLayout.addView(BottomLayoutTextView(
                    context,
                    false,
                    index != 0,
                    parts[0]
                ))
                bottomLayout.addView(BottomLayoutTextView(
                    context,
                    true,
                    false,
                    parts[1]
                ))
            }

            MaterialDialog(context, BottomSheet()).show {
                title(0, type)
                customView(0, bottomLayout, horizontalPadding = true)
            }
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matches[position]
        val matchDetails = match.details.lines()
        val type = matchDetails[0].split(": ")[1]
        val part = matchDetails[2].split(": ")[1]

        @SuppressLint("SetTextI18n")
        holder.textView.text = "$type: $part"
        if (position == matches.size - 1) holder.divider.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return matches.size
    }
}
