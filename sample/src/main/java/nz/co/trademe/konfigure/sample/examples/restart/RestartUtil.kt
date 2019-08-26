package nz.co.trademe.konfigure.sample.examples.restart

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

/**
 * Extension function for displaying a Snackbar with a restart app action.
 */
@JvmOverloads
fun AppCompatActivity.showRestartSnackbar(view: View = findViewById(android.R.id.content)): Snackbar =
    Snackbar.make(view,
        "You have pending config changes which require a restart",
        Snackbar.LENGTH_INDEFINITE)
        .setAction("Restart") {
            restartApp(this)
        }.also {
            it.show()
        }


/**
 * Function for restarting a given application and returning to the activity passed in as an argument
 */
private fun restartApp(activity: AppCompatActivity) {
    // Fetch the package manager so we can get the default launch activity
    val packageManager = activity.packageManager
        ?: throw IllegalStateException("Could not retrieve package manager.")

    // Create an intent with the default start activity for your application
    val activityIntent = packageManager.getLaunchIntentForPackage(activity.packageName)
        ?: throw IllegalStateException("Could not retrieve launch intent.")
    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

    // Create a pending intent so the application is restarted after exit(0) is called.
    val pendingIntent = PendingIntent.getActivity(activity, 0, activityIntent, PendingIntent.FLAG_CANCEL_CURRENT)

    // Use AlarmManager to call this intent in 100ms.
    val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        ?: throw IllegalStateException("Could not retrieve alarm service.")
    alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)

    // Kill the application.
    activity.finish()
    Runtime.getRuntime().exit(0)
}