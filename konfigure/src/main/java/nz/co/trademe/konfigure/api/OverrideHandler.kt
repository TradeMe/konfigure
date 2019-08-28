package nz.co.trademe.konfigure.api

/**
 * [OverrideHandler] defines a mutable [ConfigSource]. It allows setting and clearing
 * of keys in the source, which then take precedent over other [ConfigSource]s. This is
 * mainly useful for debugging, but can allow developers to expose config items to users
 * for modification.
 */
interface OverrideHandler: ConfigSource {

    fun set(key: String, value: String)

    fun clear(key: String)
}