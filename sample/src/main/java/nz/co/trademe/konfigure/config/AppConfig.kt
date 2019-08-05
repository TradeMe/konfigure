package nz.co.trademe.konfigure.config

import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.model.ConfigMetadata

class AppConfig: Config(configSources = listOf(LocalSource)) {

    data class Meta(
        val group: String,
        val title: String = "",
        val description: String = "",
        val requiresRestart: Boolean = false
    ): ConfigMetadata

    val classMeta = Meta(
        group = "Application config"
    )

    var test: String by config(
        key = "testString",
        defaultValue = "This is a test config value",
        metadata = classMeta.copy(
            title = "Test String",
            description = "A test string"
        )
    )
}