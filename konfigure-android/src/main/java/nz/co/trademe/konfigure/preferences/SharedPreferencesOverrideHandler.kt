package nz.co.trademe.konfigure.preferences

import android.content.Context
import nz.co.trademe.konfigure.api.OverrideHandler

private const val CONFIG_SHARED_PREFERENCES = "app_config"

/**
 * An implementation of a [OverrideHandler] which is backed by a [android.content.SharedPreferences] instance
 * for
 */
class SharedPreferencesOverrideHandler(context: Context): OverrideHandler {

    private val preferences = context.getSharedPreferences(CONFIG_SHARED_PREFERENCES, Context.MODE_PRIVATE)

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
