package nz.co.trademe.konfigure.api

interface ConfigSource {

    val all: Map<String, String>

    fun contains(key: String): Boolean

    fun get(key: String): String
}