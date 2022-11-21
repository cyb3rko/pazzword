package com.cyb3rko.pazzword.fragments.analyzefragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.cyb3rko.pazzword.R
import com.cyb3rko.pazzword.modals.MatchBottomSheet
import me.gosimple.nbvcxz.matching.match.Match

class MatchAdapter(
    context: Context,
    matches: List<Match>
) : ArrayAdapter<Match>(context, R.layout.match_item, matches) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val match = getItem(position)!!
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(
                R.layout.match_item,
                parent,
                false
            )
        }
        val matchDetails = match.details.lines().toMutableList()
        val type = matchDetails[0].split(": ")[1]
        val part = matchDetails[2].split(": ")[1]
        matchDetails.removeFirst()
        @SuppressLint("SetTextI18n")
        itemView!!.findViewById<TextView>(R.id.text_view).text = "$type: $part"

        itemView.setOnClickListener {
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            MatchBottomSheet(matchDetails, type).show(fragmentManager, MatchBottomSheet.TAG)
        }
        return itemView
    }
}
