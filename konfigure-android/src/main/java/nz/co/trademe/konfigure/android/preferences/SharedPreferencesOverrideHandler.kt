package nz.co.trademe.konfigure.android.preferences

import android.content.Context
import nz.co.trademe.konfigure.api.OverrideHandler

private const val CONFIG_SHARED_PREFERENCES = "app_config"

/**
 * An implementation of a [OverrideHandler] which is backed by a [android.content.SharedPreferences] instance.
 *
 * @param context The context used to get a reference to the shared preferences.
 * @param preferencesName The name used for the preferences file used by the handler.
 */
class SharedPreferencesOverrideHandler(
    context: Context,
    preferencesName: String = CONFIG_SHARED_PREFERENCES
): OverrideHandler {

    private val preferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    override fun set(key: String, value: String) {
        preferences.edit()
            .putString(key, value)
            .apply()
    }

    override fun clear(key: String) {
        preferences.edit()
            .remove(key)
            .apply()
    }

    override val all: Map<String, String>
        get() = preferences.all.mapValues { it.value.toString() }

}
