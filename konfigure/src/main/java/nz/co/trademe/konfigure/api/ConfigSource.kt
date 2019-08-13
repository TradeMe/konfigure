package nz.co.trademe.konfigure.api

/**
 * This interface defined a config source. This interface acts as a Map in essence, exposing limited
 * functionality which is used by the [Config] class.
 */
interface ConfigSource {

    val all: Map<String, String>

    fun contains(key: String): Boolean

    fun get(key: String): String
}