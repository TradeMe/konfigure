package nz.co.trademe.konfigure.api

import kotlin.properties.ReadWriteProperty

/**
 * Interface simplifying [ReadWriteProperty] usage for config items
 */
interface ConfigDelegate<T: Any>: ReadWriteProperty<ConfigRegistry, T>