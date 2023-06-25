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

package com.cyb3rko.pazzword.fragments.generatorfragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cyb3rko.pazzword.R
import com.cyb3rko.pazzword.databinding.FragmentGeneratorBinding
import com.cyb3rko.pazzword.showClipboardToast
import com.cyb3rko.pazzword.storeToClipboard
import com.google.android.material.slider.Slider
import me.gosimple.nbvcxz.resources.Generator
import java.security.SecureRandom

class GeneratorFragment : Fragment() {
    companion object {
        const val TYPE_PASSPHRASE = 1
        const val TYPE_PASSWORD = 2

        const val alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val alphaNumeric = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        const val alphaNumericSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()"
        const val numeric = "1234567890"
        const val alphaSpace = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    }

    enum class PasswordTypes {
        ALPHA,
        ALPHANUMERIC,
        ALPHANUMERICSYMBOL,
        NUMERIC,
        ALPHASPACE
    }

    private var _binding: FragmentGeneratorBinding? = null
    private lateinit var myContext: Context
    private val viewModel: GeneratorViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var selectedType = 0
    private var selectedPasswordType = PasswordTypes.ALPHANUMERIC

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typeToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
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
            viewModel.animationShown = false
            showInputElements()
        }

        val items = resources.getStringArray(R.array.password_types)
        val adapter = ArrayAdapter(myContext, R.layout.password_type_item, items)
        binding.passwordTypeInput.apply {
            setAdapter(adapter)
            setText(items[0], false)
            setOnItemClickListener { _, _, i, _ ->
                selectedPasswordType = when (i) {
                    0 -> PasswordTypes.ALPHANUMERIC
                    1 -> PasswordTypes.ALPHA
                    2 -> PasswordTypes.ALPHANUMERICSYMBOL
                    3 -> PasswordTypes.NUMERIC
                    4 -> PasswordTypes.ALPHASPACE
                    else -> PasswordTypes.ALPHANUMERIC
                }
                viewModel.passwordType = selectedPasswordType
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
                if (selectedType == TYPE_PASSPHRASE) {
                    viewModel.passphraseLength = slider.value
                } else {
                    viewModel.passwordLength = slider.value
                }
                updateSliderLengthText(slider.value.toInt())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.animationShown) {
            showInputElements()
            val items = resources.getStringArray(R.array.password_types)
            val adapter = ArrayAdapter(myContext, R.layout.password_type_item, items)
            binding.passwordTypeInput.setAdapter(adapter)
            selectedPasswordType = viewModel.passwordType
        }
    }

    private fun showInputElements() {
        binding.apply {
            animationView.visibility = View.GONE
            animationView.cancelAnimation()
            slider.visibility = View.VISIBLE
            divider2.visibility = View.VISIBLE
            output.visibility = View.VISIBLE
        }
    }

    private fun showPassphraseItems(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        binding.apply {
            delimiterInput.visibility = visibility
            if (visible) {
                slider.valueTo = 8f
                slider.valueFrom = 3f
                slider.value = viewModel.passphraseLength
                updateSliderLengthText(viewModel.passphraseLength.toInt())
            }
        }
    }

    private fun showPasswordItems(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        binding.apply {
            passwordType.visibility = visibility
            if (visible) {
                slider.valueTo = 64f
                slider.valueFrom = 6f
                slider.value = viewModel.passwordLength
                updateSliderLengthText(viewModel.passwordLength.toInt())
            }
        }
    }

    private fun generatePassphrase() {
        val delimiter = binding.delimiterInputText.text.toString()
        val length = binding.slider.value
        binding.output.text = Generator.generatePassphrase(delimiter, length.toInt())
    }

    private fun generatePassword() {
        val length = binding.slider.value
        binding.output.text = generatePassword(selectedPasswordType, length.toInt())
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

    private fun generatePassword(passwordType: PasswordTypes, length: Int): String {
        val stringBuilder = StringBuilder()
        val characters = when (passwordType) {
            PasswordTypes.ALPHA -> alpha
            PasswordTypes.ALPHANUMERIC -> alphaNumeric
            PasswordTypes.ALPHANUMERICSYMBOL -> alphaNumericSymbols
            PasswordTypes.NUMERIC -> numeric
            PasswordTypes.ALPHASPACE -> alphaSpace
        }

        val characterLength = characters.length
        val secureRandom = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SecureRandom.getInstanceStrong()
        } else {
            SecureRandom()
        }

        repeat(length) {
            val index = secureRandom.nextInt(characterLength)
            stringBuilder.append(characters[index])
        }

        return stringBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
