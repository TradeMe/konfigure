package nz.co.trademe.konfigure.api

interface OverrideHandler: ConfigSource {

    fun set(key: String, value: String)

    fun clear(key: String)
}