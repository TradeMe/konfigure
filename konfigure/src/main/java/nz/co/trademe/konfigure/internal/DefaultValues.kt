package nz.co.trademe.konfigure.internal

import kotlin.reflect.KClass

/**
 * Looks up a sensible default value for this class. Will throw an exception
 * if applied to an unsupported class.
 */
@Suppress("UNCHECKED_CAST")
internal val <T: Any> KClass<T>.defaultValue: T
    get() {
        return when (this) {
            String::class -> "" as T
            Boolean::class -> false as T
            Int::class -> 0 as T
            Long::class -> 0L as T
            Float::class -> 0F as T
            Double::class -> 0.0 as T
            else -> throw IllegalArgumentException("Unsupported type $this")
        }
    }