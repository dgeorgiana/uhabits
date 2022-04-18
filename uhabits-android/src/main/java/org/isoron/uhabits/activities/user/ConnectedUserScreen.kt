package org.isoron.uhabits.activities.user

import org.isoron.uhabits.intents.IntentFactory
import org.isoron.uhabits.utils.startActivitySafely

class ConnectedUserScreen(
    private val activity: UserProfileActivity,
    private val intents: IntentFactory
    ) {

        fun showSyncWithGoogleDrive() =
            activity.startActivitySafely(intents.syncWithGoogleDrive(activity))

        fun showSinkWithGoogleSheets() =
            activity.startActivitySafely(intents.syncWithGoogleSheets(activity))

}