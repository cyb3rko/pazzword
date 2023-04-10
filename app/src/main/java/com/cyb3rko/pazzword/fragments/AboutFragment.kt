package com.cyb3rko.pazzword.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cyb3rko.pazzword.BuildConfig
import com.cyb3rko.pazzword.R
import com.cyb3rko.pazzword.openURL
import com.mikepenz.aboutlibraries.LibsBuilder
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val githubIcon = mehdi.sakout.aboutpage.R.drawable.about_icon_github
        val emailIcon = mehdi.sakout.aboutpage.R.drawable.about_icon_email

        return AboutPage(context)
            .setImage(R.mipmap.ic_launcher_foreground)
            .setDescription(getString(R.string.about_description))
            .addItem(
                Element(
                    String.format(
                        getString(R.string.about_element_version),
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE
                    ),
                    githubIcon
                ).setOnClickListener(showChangelog())
            )
            .addGroup(getString(R.string.about_group_legal))
            .addItem(
                Element(
                    getString(R.string.about_element_libraries),
                    R.drawable._ic_libraries
                ).setOnClickListener(showLibraries())
            )
            .addItem(
                Element(
                    getString(R.string.about_element_icons),
                    R.drawable._ic_question
                ).setOnClickListener(showIcons())
            )
            .addItem(
                Element(
                    getString(R.string.about_element_animations),
                    R.drawable._ic_question
                ).setOnClickListener(showAnimations())
            )
            .addGroup(getString(R.string.about_group_connect))
            .addItem(
                Element(
                    getString(R.string.about_element_feedback_text),
                    githubIcon
                ).setOnClickListener(openGithubFeedback())
            )
            .addItem(
                Element(
                    getString(R.string.about_element_email_text),
                    emailIcon
                ).setOnClickListener(writeEmail())
            )
            .addItem(
                Element(
                    getString(R.string.about_element_github_text),
                    githubIcon
                ).setOnClickListener(openGithubProfile())
            )
            .create()
    }

    private fun showChangelog() = View.OnClickListener {
        openURL("https://github.com/cyb3rko/pazzword/releases")
    }

    private fun showLibraries() = View.OnClickListener {
        context?.let { validContext ->
            LibsBuilder()
                .withLicenseShown(true)
                .withAboutIconShown(false)
                .withAboutVersionShown(false)
                .withActivityTitle(getString(R.string.about_element_libraries))
                .withSearchEnabled(true)
                .start(validContext)
        }
    }

    private fun showIcons() = View.OnClickListener {
        findNavController().navigate(R.id.navigation_about_icons)
    }

    private fun showAnimations() = View.OnClickListener {
        findNavController().navigate(R.id.navigation_about_animations)
    }

    private fun openGithubFeedback() = View.OnClickListener {
        openURL("https://github.com/cyb3rko/pazzword")
    }

    private fun openGithubProfile() = View.OnClickListener {
        openURL("https://github.com/cyb3rko")
    }

    private fun writeEmail() = View.OnClickListener {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:niko@cyb3rko.de")
        }
        startActivity(intent)
    }
}
