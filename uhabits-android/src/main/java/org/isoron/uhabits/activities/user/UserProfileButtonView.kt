package org.isoron.uhabits.activities.user

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import org.isoron.uhabits.databinding.IcActivityUserProfileBinding

@SuppressLint("ViewConstructor")
class UserProfileButtonView (
    context: Context
) : FrameLayout(context) {

    private var binding = IcActivityUserProfileBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }
}