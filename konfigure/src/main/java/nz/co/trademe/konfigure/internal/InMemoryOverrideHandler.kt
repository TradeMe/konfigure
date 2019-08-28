package nz.co.trademe.konfigure.internal

import nz.co.trademe.konfigure.api.OverrideHandler

/**
 * A simple implementation of an [OverrideHandler], backed by a [MutableMap].
 *
 * This class is used as the default [OverrideHandler] for the konfigure library. This allows
 * developers to use overrides within code, and have them persist in-memory with the [Config] instance.
 */
internal class InMemoryOverrideHandler: OverrideHandler {

    private val backingMap: MutableMap<String, String> = mutableMapOf()

    override fun set(key: String, value: String) {
        backingMap[key] = value
    }

    override fun clear(key: String) {
        backingMap.remove(key)
    }

    override val all: Map<String, String>
        get() = backingMap

}