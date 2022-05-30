/*
 * Copyright (C) 2016-2021 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.activities.intro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import org.isoron.uhabits.R
import org.isoron.uhabits.activities.habits.list.ListHabitsActivity

/**
 * Activity that introduces the app to the user, shown only after the app is
 * launched for the first time.
 */
class IntroActivity : AppIntro2() {

    // Canal de notificari
    var CHANNEL_ID: String = "my_notification_channel"
    var NOTIFICATION_ID: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStatusBar(false)

        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_title_1),
                getString(R.string.intro_description_1),
                R.drawable.intro_icon_1,
                Color.parseColor("#194673")
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_title_2),
                getString(R.string.intro_description_2),
                R.drawable.intro_icon_2,
                Color.parseColor("#ffa726")
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_title_4),
                getString(R.string.intro_description_4),
                R.drawable.intro_icon_4,
                Color.parseColor("#9575cd")
            )
        )

        // Crearea canalului pentru notificari este realizata cat mai devreme in aplicatie
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Crearea canalului de notificari
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }
}
