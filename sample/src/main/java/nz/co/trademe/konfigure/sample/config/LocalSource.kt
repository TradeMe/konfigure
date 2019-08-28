package nz.co.trademe.konfigure.sample.config

import nz.co.trademe.konfigure.api.ConfigSource

object LocalSource: ConfigSource {

    override val all: Map<String, String>
        get() = mapOf(
            "testBoolean" to "true",
            "testString" to "something"
        )
}