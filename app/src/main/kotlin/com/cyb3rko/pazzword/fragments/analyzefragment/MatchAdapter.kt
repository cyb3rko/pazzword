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
        matchDetails.removeAt(0)
        @SuppressLint("SetTextI18n")
        itemView!!.findViewById<TextView>(R.id.text_view).text = "$type: $part"

        itemView.setOnClickListener {
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            MatchBottomSheet(matchDetails, type).show(fragmentManager, MatchBottomSheet.TAG)
        }
        return itemView
    }
}
