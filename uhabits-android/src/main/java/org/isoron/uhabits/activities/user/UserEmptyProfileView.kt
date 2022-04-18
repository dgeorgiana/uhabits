package org.isoron.uhabits.activities.user

import org.isoron.uhabits.databinding.ShowEmptyUserProfileBinding
import org.isoron.uhabits.core.models.PaletteColor
import org.isoron.uhabits.utils.currentTheme
import org.isoron.uhabits.utils.setupToolbar
import android.annotation.SuppressLint
import org.isoron.uhabits.BuildConfig
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.content.Context
import org.isoron.uhabits.R

@SuppressLint("ViewConstructor")
class UserEmptyProfileView(
    context: Context,
    private val screen: EmptyUserScreen,
    ) : FrameLayout(context) {

        private var binding = ShowEmptyUserProfileBinding.inflate(LayoutInflater.from(context))

        init {
            addView(binding.root)
            setupToolbar(
                toolbar = binding.toolbar,
                color = PaletteColor(18),
                title = resources.getString(R.string.user_profile_title),
                theme = currentTheme(),
            )
            val profileTitle = resources.getString(R.string.user_profile_title)
            binding.profileTitle.text = String.format(profileTitle, BuildConfig.VERSION_NAME)
            binding.emptySpace.text = String.format("", BuildConfig.VERSION_NAME)

            // Actiune in contul Google la click
            //binding.connectToGoogleAccount.setOnClickListener { (screen.connectToGoogleAccount()) }

        }
    }