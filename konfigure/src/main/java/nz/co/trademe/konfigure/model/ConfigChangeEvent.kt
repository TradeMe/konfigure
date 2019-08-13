package nz.co.trademe.konfigure.model

/**
 * Event model emitted when a config item is changed
 */
data class ConfigChangeEvent<T>(
    val key: String,
    val oldValue: T,
    val newValue: T,
    val metadata: ConfigMetadata
)
