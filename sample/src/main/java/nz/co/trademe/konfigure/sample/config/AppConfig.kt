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

    var anotherOne: String by config()

    val testSubConfig = TestSubConfig(parent = this)

    val restartableSubconfig = RestartableSubconfig(parent = this)
}