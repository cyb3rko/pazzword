package com.cyb3rko.pazzword.fragments.generatorfragment

import androidx.lifecycle.ViewModel
import com.cyb3rko.pazzword.fragments.generatorfragment.GeneratorFragment.PasswordTypes

internal class GeneratorViewModel : ViewModel() {
    var animationShown: Boolean = true
    var passphraseLength: Float = 3f
    var passwordType: PasswordTypes = PasswordTypes.ALPHANUMERIC
    var passwordLength: Float = 12f
}
