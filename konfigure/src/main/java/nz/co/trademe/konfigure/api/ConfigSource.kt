package nz.co.trademe.konfigure.api

/**
 * This interface defines a config source. This interface acts as a Map in essence, exposing limited
 * functionality which is used by the [Config] class.
 */
interface ConfigSource {

    val all: Map<String, String>

}