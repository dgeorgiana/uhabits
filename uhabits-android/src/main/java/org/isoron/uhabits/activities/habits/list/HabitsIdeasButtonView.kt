package org.isoron.uhabits.activities.habits.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import org.isoron.uhabits.databinding.IcActivityHabitsIdeasBinding

@SuppressLint("ViewConstructor")
class HabitsIdeasButtonView (
    context: Context
) : FrameLayout(context) {

    private var binding = IcActivityHabitsIdeasBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }
}
