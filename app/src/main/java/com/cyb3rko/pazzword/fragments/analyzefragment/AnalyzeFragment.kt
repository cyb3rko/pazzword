package com.cyb3rko.pazzword.fragments.analyzefragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyb3rko.pazzword.R
import com.cyb3rko.pazzword.databinding.FragmentAnalyzeBinding
import com.cyb3rko.pazzword.hideKeyboard
import com.cyb3rko.pazzword.round
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.gosimple.nbvcxz.Nbvcxz
import me.gosimple.nbvcxz.matching.match.Match
import me.gosimple.nbvcxz.resources.*
import me.gosimple.nbvcxz.resources.Dictionary
import me.gosimple.nbvcxz.scoring.Result
import me.gosimple.nbvcxz.scoring.TimeEstimate

class AnalyzeFragment : Fragment() {
    private var _binding: FragmentAnalyzeBinding? = null
    private lateinit var myContext: Context
    private lateinit var nbvcxz: Nbvcxz

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        val root = binding.root
        myContext = requireContext()

        binding.searchInput.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard()
                val text = (v as TextInputEditText).text.toString()
                if (text.isNotBlank()) {
                    GlobalScope.launch {
                        if (!this@AnalyzeFragment::nbvcxz.isInitialized) initializeNbvcxz()
                        activity?.runOnUiThread {
                            hideWaitingAnimation()
                            estimatePassword(text)
                        }
                    }
                }
                return@setOnKeyListener true
            }
            false
        }

        return root
    }

    private fun initializeNbvcxz() {
        val dictionary = DictionaryBuilder()
            .setDictionaryName("custom_passwords")
            .addWords(resources.getStringArray(R.array.passwords).toList(), 200)
            .setExclusion(false)
            .createDictionary()

        val dictionaries = listOf(
            Dictionary("passwords", DictionaryUtil.loadRankedDictionary(DictionaryUtil.passwords), false),
            Dictionary("male_names", DictionaryUtil.loadRankedDictionary(DictionaryUtil.male_names), false),
            Dictionary("female_names", DictionaryUtil.loadRankedDictionary(DictionaryUtil.female_names), false),
            Dictionary("surnames", DictionaryUtil.loadRankedDictionary(DictionaryUtil.surnames), false),
            Dictionary("english", DictionaryUtil.loadRankedDictionary(DictionaryUtil.english), false),
            Dictionary("eff_large", DictionaryUtil.loadRankedDictionary(DictionaryUtil.eff_large), false),
            dictionary
        )

        val configuration = ConfigurationBuilder()
            .setDictionaries(dictionaries)
            .createConfiguration()

        nbvcxz = Nbvcxz(configuration)
    }

    private fun estimatePassword(password: String) {
        val result = nbvcxz.estimate(password)
        showResults(result, result.feedback)
        binding.scrollView.smoothScrollTo(0,0)
    }

    private fun showResults(result: Result, feedback: Feedback) {
        if (feedback.result != "main.feedback.insecure") {
            showSecurityBanner(true)
        } else {
            showSecurityBanner(false)
        }
        binding.apply {
            if (feedback.warning != null) {
                warning.text = feedback.warning
                warningContainer.visibility = View.VISIBLE
            } else {
                warningContainer.visibility = View.GONE
            }
            informationContainer.visibility = View.VISIBLE
            @SuppressLint("SetTextI18n")
            basicScore.text = "${result.basicScore} / 4"
            entropy.text = result.entropy.round(2).toString()
            random.text = result.isRandom.toString()
            crackingContainer.visibility = View.VISIBLE
            setCrackingResults(result)
            if (feedback.suggestion.isNotEmpty()) {
                fillSuggestions(feedback.suggestion)
            }
            if (result.matches.isNotEmpty()) {
                fillMatches(result.matches)
            }
            result.matches[0].details.lines()
        }
    }

    private fun setCrackingResults(result: Result) {
        var guessesCount = result.guesses
        var repititions = 0
        val number: String
        if (guessesCount.compareTo(BigDecimal(999999)) == 1) {
            val bigD10 = BigDecimal(10)
            var comparison = guessesCount.compareTo(bigD10)
            while (comparison == 1 || comparison == 0) {
                guessesCount = guessesCount.divide(BigDecimal.TEN)
                repititions++
                comparison = guessesCount.compareTo(bigD10)
            }
            number = guessesCount.toDouble().round(2).toString() + " * " + getSuperScripted10(repititions)
        } else {
            number = guessesCount.toString()
        }
        binding.apply {
            guesses.text = number
            offlineMd5.text = TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_MD5")
            offlineSha512.text = TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_SHA512")
            offlineBcrypt10.text = TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_10")
            offlineBcrypt14.text = TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_14")
            onlineUnthrottled.text = TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_UNTHROTTLED")
            onlineThrottled.text = TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_THROTTLED")
        }
    }

    private fun getSuperScripted10(repititions: Int): String {
        val codes = mapOf(
            '0' to "\u2070",
            '1' to "\u00B9",
            '2' to "\u00B2",
            '3' to "\u00B3",
            '4' to "\u2074",
            '5' to "\u2075",
            '6' to "\u2076",
            '7' to "\u2077",
            '8' to "\u2078",
            '9' to "\u2079"
        )
        var output = "10"
        repititions.toString().forEach {
            output += codes[it]
        }
        return output
    }

    private fun fillSuggestions(suggestions: List<String>) {
        binding.apply {
            if (suggestionsRecycler.layoutManager == null) {
                suggestionsRecycler.layoutManager = object: LinearLayoutManager(myContext) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
            }
            suggestionsRecycler.adapter = SuggestionAdapter(suggestions)
            suggestionsContainer.visibility = View.VISIBLE
        }
    }

    private fun fillMatches(matches: List<Match>) {
        binding.apply {
            if (matchesRecycler.layoutManager == null) {
                matchesRecycler.layoutManager = object: LinearLayoutManager(myContext) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
            }
            matchesRecycler.adapter = MatchAdapter(myContext, matchesRecycler, matches)
            matchesContainer.visibility = View.VISIBLE
        }
    }

    private fun showSecurityBanner(secure: Boolean) {
        val maxFrame = if (secure) 80 else 50
        val animation = if (secure) "secure.json" else "insecure.json"
        val text = getString(if (secure) R.string.security_banner_secure else R.string.security_banner_insecure)
        if (secure) {
            binding.securityBanner.background
        }
        binding.apply {
            securityBanner.background = if (secure) {
                ResourcesCompat.getDrawable(resources, R.drawable.sec_banner_bg_green, activity?.theme)
            } else {
                ResourcesCompat.getDrawable(resources, R.drawable.sec_banner_bg_red, activity?.theme)
            }
            animationView.apply {
                setAnimation(animation)
                playAnimation()
                setMaxFrame(maxFrame)
            }
            securityText.text = text
            securityCard.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideWaitingAnimation() {
        binding.waitingAnimationView.apply {
            visibility = View.GONE
            cancelAnimation()
        }
    }
}
