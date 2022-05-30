package org.isoron.uhabits.activities.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import org.isoron.uhabits.HabitsApplication
import org.isoron.uhabits.R
import org.isoron.uhabits.activities.AndroidThemeSwitcher
import kotlin.coroutines.CoroutineContext


class UserProfileActivity : AppCompatActivity(), CoroutineScope {
    var isConnected: Boolean? = false
    lateinit var connectToGoogleButton: SignInButton
    lateinit var task: Task<GoogleSignInAccount>
    lateinit var account: GoogleSignInAccount
    val RC_SIGN_IN = 7

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as HabitsApplication
        val extras = intent.extras
        if (extras != null) {
            isConnected = extras.getBoolean("isConnected");
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = mGoogleSignInClient.signInIntent

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            val connectedUserScreen = ConnectedUserScreen(
                this,
                app.component.intentFactory
            )
            setContentView(ConnectedUserProfileView(this, connectedUserScreen));
        } else {
            // User-ul nu este inca conectat
            val emptyUserScreen = EmptyUserScreen(
                this,
                app.component.intentFactory
            )
            setContentView(UserEmptyProfileView(this, emptyUserScreen))

            // View schimbat la apasarea butonului de conectare la contul de Google
            connectToGoogleButton = findViewById(R.id.connect_to_google_account)
            connectToGoogleButton.setOnClickListener {
                startActivityForResult(signInIntent, RC_SIGN_IN)
                val app = application as HabitsApplication
                val connectedUserScreen = ConnectedUserScreen(
                    this,
                    app.component.intentFactory
                )
                setContentView(ConnectedUserProfileView(this, connectedUserScreen))
            }
        }
        if (account != null) {
            val info: View = findViewById(R.id.infoButton)
            info.setOnClickListener { view ->
                Snackbar.make(
                    view, "User Profile Info: \n" + account.givenName + ", " +
                            account.familyName + ", " + account.email,
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Action", null)
                    .show()
            }

            val logout: View = findViewById(R.id.logoutButton)
            logout.setOnClickListener { view ->
                mGoogleSignInClient.signOut()
                val emptyUserScreen = EmptyUserScreen(
                    this,
                    app.component.intentFactory
                )
                setContentView(UserEmptyProfileView(this, emptyUserScreen))
            }
        }

        AndroidThemeSwitcher(this, app.component.preferences).apply()

        launch {
            async(Dispatchers.Default) {
                //Working on background thread
                /* Refacerea variabilei se realizeaza pe un alt thread pentru a permite
                    logarea la Google pe thread-ul de main */
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 7) {
            task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                account = task.getResult(ApiException::class.java)
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            }
        }
    }
}