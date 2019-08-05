package nz.co.trademe.konfigure.config

import nz.co.trademe.konfigure.api.ConfigSource
import java.lang.IllegalArgumentException

object LocalSource: ConfigSource {
    override fun contains(key: String): Boolean = all.containsKey(key)

    override fun get(key: String): String = all.getOrElse(key) { throw IllegalArgumentException("Key not found: $key") }

    override val all: Map<String, String>
        get() = mapOf(
            "testBoolean" to "true",
            "testString" to "something"
        )
}