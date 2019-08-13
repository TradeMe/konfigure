package nz.co.trademe.konfigure.config

import nz.co.trademe.konfigure.api.OverrideHandler

class LocalOverrideHandler: OverrideHandler {
    override fun set(key: String, value: String) {
        (all as MutableMap)[key] = value
    }

    override fun clear(key: String) {
        (all as MutableMap).remove(key)
    }

    override val all: Map<String, String> = mutableMapOf()

    override fun contains(key: String): Boolean {
        return (all as MutableMap).contains(key)
    }

    override fun get(key: String): String {
        return (all as MutableMap)[key]!!
    }
}