package org.isoron.uhabits.activities.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.isoron.uhabits.HabitsApplication
import org.isoron.uhabits.R
import org.isoron.uhabits.activities.AndroidThemeSwitcher
import kotlin.coroutines.CoroutineContext


class UserProfileActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferencesFile", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()

        super.onCreate(savedInstanceState)
        val app = application as HabitsApplication
        if (sharedPreferences.getBoolean("isConnected", false)) {
            val connectedUserScreen = ConnectedUserScreen(
                this,
                app.component.intentFactory
            )
            setContentView(ConnectedUserProfileView(this, connectedUserScreen));
        } else {
            editor.putBoolean("isConnected", false)
            editor.apply()
            editor.commit()
            val emptyUserScreen = EmptyUserScreen(
                this,
                app.component.intentFactory
            )
            setContentView(UserEmptyProfileView(this, emptyUserScreen))

            // View schimbat la apasarea butonului de conectare la contul de Google

            val connectToGoogleButton: Button = findViewById(R.id.connect_to_google_account)
            connectToGoogleButton.setOnClickListener {
                editor.putBoolean("isConnected",true)
                editor.apply()
                editor.commit()
                startActivity(Intent(this@UserProfileActivity, UserProfileActivity::class.java))
            }
        }

        AndroidThemeSwitcher(this, app.component.preferences).apply()

        launch {
            async(Dispatchers.Default) {
                //Working on background thread
                if (sharedPreferences.getBoolean("isConnected", false)) {
                    /* Refacerea variabilei se realizeaza pe un alt thread pentru a permite
                    logarea la Google pe thread-ul de main */

                    editor.putBoolean("isConnected",false)
                    editor.apply()
                    editor.commit()
                }
            }
        }
    }
}