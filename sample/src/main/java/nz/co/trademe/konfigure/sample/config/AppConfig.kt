package nz.co.trademe.konfigure.sample.config

import android.content.Context
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.android.extensions.config
import nz.co.trademe.konfigure.android.preferences.SharedPreferencesOverrideHandler

class AppConfig(context: Context): Config(
    configSources = listOf(LocalSource),
    overrideHandler = SharedPreferencesOverrideHandler(context)
) {

    var test: String by config(
        key = "testString",
        defaultValue = "This is a test config value",
        title = "Test String",
        description = "This is a test string"
    )

    /**
     * We can define config items by defaulting the key to be the name of the property.
     * In this case, this property will look up a String value keyed by "anotherOne"
     */
    var anotherOne: String by config()

    val testSubConfig = TestSubConfig(parent = this)

    val restartableSubconfig = RestartableSubconfig(parent = this)
}