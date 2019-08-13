package nz.co.trademe.konfigure.config

import android.content.Context
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.extensions.config
import nz.co.trademe.konfigure.preferences.SharedPreferencesOverrideHandler

class AppConfig(context: Context): Config(
    configSources = listOf(LocalSource),
    overrideHandler = SharedPreferencesOverrideHandler(context)
) {

    var test: String by config(
        key = "testString",
        defaultValue = "This is a test config value",
        title = "Test String",
        description = "This is a test string",
        group = "Demo"
    )

    var test2: Boolean by config(
        key = "testBool",
        defaultValue = false,
        title = "Test Boolean",
        description = "This is a test boolean",
        group = "Demo"
    )
}