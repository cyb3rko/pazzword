package com.cyb3rko.pazzword.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.cyb3rko.pazzword.R
import com.cyb3rko.pazzword.databinding.FragmentRankingBinding
import com.cyb3rko.pazzword.showClipboardToast
import com.cyb3rko.pazzword.storeToClipboard

class RankingFragment : Fragment() {
    private var _binding: FragmentRankingBinding? = null
    private lateinit var myContext: Context

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        val root = binding.root
        myContext = requireContext()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = resources.getStringArray(R.array.rank)
        val adapter = ArrayAdapter(myContext, android.R.layout.simple_list_item_1, list)
        binding.list.adapter = adapter
        binding.list.setOnItemClickListener { parent, _, position, _ ->
            val password = (parent.getItemAtPosition(position) as String).split(" ")[1]
            storeToClipboard(getString(R.string.top200_passwords_clipboard_label), password)
            showClipboardToast(getString(R.string.top200_passwords_toast_label))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
