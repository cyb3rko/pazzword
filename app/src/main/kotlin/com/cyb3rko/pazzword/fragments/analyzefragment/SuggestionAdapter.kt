package com.cyb3rko.pazzword.fragments.analyzefragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyb3rko.pazzword.R
import com.google.android.material.divider.MaterialDivider

class SuggestionAdapter(
    private var messages: List<String>
): RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_view)
        val divider: MaterialDivider = itemView.findViewById(R.id.divider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.suggestion_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = messages[position]
        if (position == messages.size - 1) holder.divider.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
