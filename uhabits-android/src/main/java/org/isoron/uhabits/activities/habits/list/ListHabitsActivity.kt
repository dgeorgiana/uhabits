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

package org.isoron.uhabits.activities.habits.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_list_habits_ideas.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.isoron.uhabits.*
import org.isoron.uhabits.activities.habits.list.views.HabitCardListAdapter
import org.isoron.uhabits.activities.intro.NotificationUtils
import org.isoron.uhabits.activities.user.UserProfileActivity
import org.isoron.uhabits.core.preferences.Preferences
import org.isoron.uhabits.core.tasks.TaskRunner
import org.isoron.uhabits.core.ui.ThemeSwitcher.Companion.THEME_DARK
import org.isoron.uhabits.core.utils.MidnightTimer
import org.isoron.uhabits.database.AutoBackup
import org.isoron.uhabits.inject.ActivityContextModule
import org.isoron.uhabits.inject.DaggerHabitsActivityComponent
import org.isoron.uhabits.utils.restartWithFade
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback

class ListHabitsActivity : AppCompatActivity(), Preferences.Listener {

    var pureBlack: Boolean = false
    lateinit var taskRunner: TaskRunner
    lateinit var adapter: HabitCardListAdapter
    lateinit var rootView: ListHabitsRootView
    lateinit var screen: ListHabitsScreen
    lateinit var prefs: Preferences
    lateinit var midnightTimer: MidnightTimer
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var menu: ListHabitsMenu

    private val calendar = Calendar.getInstance()
    private var mNotificationTime: Long = 0
    private var mNotified = false

    override fun onQuestionMarksChanged() {
        invalidateOptionsMenu()
        menu.behavior.onPreferencesChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as HabitsApplication).component
        val component = DaggerHabitsActivityComponent
            .builder()
            .activityContextModule(ActivityContextModule(this))
            .habitsApplicationComponent(appComponent)
            .build()
        component.themeSwitcher.apply()

        prefs = appComponent.preferences
        prefs.addListener(this)
        pureBlack = prefs.isPureBlackEnabled
        midnightTimer = appComponent.midnightTimer
        rootView = component.listHabitsRootView
        screen = component.listHabitsScreen
        adapter = component.habitCardListAdapter
        taskRunner = appComponent.taskRunner
        menu = component.listHabitsMenu
        Thread.setDefaultUncaughtExceptionHandler(BaseExceptionHandler(this))
        component.listHabitsBehavior.onStartup()
        setContentView(rootView)

        if (!mNotified) {
            //Setarea orei de notificare (in varianta curenta, ora de notificare este setata la pornirea aplicatiei)
            mNotificationTime = calendar.timeInMillis + 20 * 1000L //20 de secunde de la pornirea aplicatiei
            NotificationUtils().setNotification(mNotificationTime, this@ListHabitsActivity)
        }
        // Activitatea accesata la apasarea butonului de profil (UserProfileActivity)
        val userButton: ImageButton = findViewById(R.id.user_profile_button)
        userButton.setOnClickListener {
            startActivity(Intent(this@ListHabitsActivity, UserProfileActivity::class.java))
        }

        // Activitatea accesata la apasarea butonului de profil (ListHabitsIdeasActivity)
        val ideasButton: ImageButton = findViewById(R.id.ideas_button)
        ideasButton.setOnClickListener {
            startActivity(Intent(this@ListHabitsActivity, ListHabitsIdeasActivity::class.java))
        }
    }

    override fun onPause() {
        midnightTimer.onPause()
        screen.onDetached()
        adapter.cancelRefresh()
        super.onPause()
    }

    override fun onResume() {
        adapter.refresh()
        screen.onAttached()
        rootView.postInvalidate()
        midnightTimer.onResume()
        taskRunner.run {
            AutoBackup(this@ListHabitsActivity).run()
        }
        if (prefs.theme == THEME_DARK && prefs.isPureBlackEnabled != pureBlack) {
            restartWithFade(ListHabitsActivity::class.java)
        }
        super.onResume()
    }

    override fun onCreateOptionsMenu(m: Menu): Boolean {
        menu.onCreate(menuInflater, m)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        invalidateOptionsMenu()
        return menu.onItemSelected(item)
    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?) {
        super.onActivityResult(request, result, data)
        screen.onResult(request, result, data)
    }
}
