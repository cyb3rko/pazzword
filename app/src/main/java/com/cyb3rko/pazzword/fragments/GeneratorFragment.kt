package com.cyb3rko.pazzword.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.cyb3rko.pazzword.R
import com.cyb3rko.pazzword.databinding.FragmentGeneratorBinding
import com.cyb3rko.pazzword.showClipboardToast
import com.cyb3rko.pazzword.storeToClipboard
import com.google.android.material.slider.Slider
import me.gosimple.nbvcxz.resources.Generator

class GeneratorFragment : Fragment() {
    private var _binding: FragmentGeneratorBinding? = null
    private lateinit var myContext: Context

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var selectedType = 0
    private var selectedPasswordType = Generator.CharacterTypes.ALPHANUMERIC

    companion object {
        const val TYPE_PASSPHRASE = 1
        const val TYPE_PASSWORD = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneratorBinding.inflate(inflater, container, false)
        val root = binding.root
        myContext = requireContext()

        return root
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typeToggleGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.passphrase_toggle -> {
                    selectedType = TYPE_PASSPHRASE
                    showPasswordItems(false)
                    showPassphraseItems(true)
                }
                R.id.password_toggle -> {
                    selectedType = TYPE_PASSWORD
                    showPassphraseItems(false)
                    showPasswordItems(true)
                }
            }
            binding.apply {
                animationView.visibility = View.GONE
                animationView.cancelAnimation()
                slider.visibility = View.VISIBLE
                divider2.visibility = View.VISIBLE
                output.visibility = View.VISIBLE
            }
        }

        val items = resources.getStringArray(R.array.password_types)
        val adapter = ArrayAdapter(myContext, R.layout.password_type_item, items)
        binding.passwordTypeInput.apply {
            setAdapter(adapter)
            setText(items[0], false)
            setOnItemClickListener { _, _, i, _ ->
                selectedPasswordType = when (i) {
                    0 -> Generator.CharacterTypes.ALPHANUMERIC
                    1 -> Generator.CharacterTypes.ALPHA
                    2 -> Generator.CharacterTypes.ALPHANUMERICSYMBOL
                    3 -> Generator.CharacterTypes.NUMERIC
                    else -> Generator.CharacterTypes.ALPHANUMERIC
                }
            }
        }

        binding.generateButton.setOnClickListener {
            when (selectedType) {
                TYPE_PASSPHRASE -> generatePassphrase()
                TYPE_PASSWORD -> generatePassword()
            }
        }

        binding.output.setOnClickListener {
            storeToClipboard((it as Button).text.toString())
        }

        binding.slider.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}

            override fun onStopTrackingTouch(slider: Slider) {
                updateSliderLengthText(slider.value.toInt())
            }
        })
    }

    private fun showPassphraseItems(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        binding.apply {
            delimiterInput.visibility = visibility
            if (visible) {
                slider.valueTo = 8f
                slider.valueFrom = 3f
                slider.value = 3f
            }
            updateSliderLengthText(3)
        }
    }

    private fun showPasswordItems(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        binding.apply {
            passwordType.visibility = visibility
            if (visible) {
                slider.valueTo = 64f
                slider.valueFrom = 6f
                slider.value = 12f
            }
            updateSliderLengthText(12)
        }
    }

    private fun generatePassphrase() {
        val delimiter = binding.delimiterInputText.text.toString()
        val length = binding.slider.value
        binding.output.text = Generator.generatePassphrase(delimiter, length.toInt())
    }

    private fun generatePassword() {
        val length = binding.slider.value
        binding.output.text = Generator.generateRandomPassword(selectedPasswordType, length.toInt())
    }

    private fun storeToClipboard(text: String) {
        val label = getString(if (selectedType == TYPE_PASSPHRASE) R.string.passphrase else R.string.password)
        storeToClipboard(label, text)
        val clipboardText = getString(if (selectedType == TYPE_PASSPHRASE) {
            R.string.passphrase_clipboard
        } else {
            R.string.password_clipboard
        })
        showClipboardToast(clipboardText)
    }

    private fun updateSliderLengthText(length: Int) {
        binding.sliderText.text = getString(R.string.length, ": $length")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
