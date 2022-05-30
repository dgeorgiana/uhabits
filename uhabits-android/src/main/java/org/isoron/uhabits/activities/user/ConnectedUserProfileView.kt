package org.isoron.uhabits.activities.user

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.show_connected_user_profile.view.*
import org.isoron.uhabits.BuildConfig
import org.isoron.uhabits.R
import org.isoron.uhabits.core.models.PaletteColor
import org.isoron.uhabits.databinding.ShowConnectedUserProfileBinding
import org.isoron.uhabits.utils.currentTheme
import org.isoron.uhabits.utils.setupToolbar


@SuppressLint("ViewConstructor")
class ConnectedUserProfileView (
    context: Context,
    private val screen: ConnectedUserScreen,
) : FrameLayout(context) {

    private var binding = ShowConnectedUserProfileBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
        setupToolbar(
            toolbar = binding.toolbar,
            color = PaletteColor(18),
            title = resources.getString(R.string.user_profile_title),
            theme = currentTheme(),
        )
        val profileTitle = resources.getString(R.string.user_profile_title)
        val emptySpace = ""
        binding.profileTitle.text = String.format(profileTitle, BuildConfig.VERSION_NAME)
        binding.emptySpace.text = String.format(emptySpace, BuildConfig.VERSION_NAME)

        // Sunt desfasurate alte activitati la click (TODO)
        binding.googleDrive.setOnClickListener { (screen.showSyncWithGoogleDrive()) }
        binding.googleSheets.setOnClickListener { (screen.showSinkWithGoogleSheets()) }
    }
}
