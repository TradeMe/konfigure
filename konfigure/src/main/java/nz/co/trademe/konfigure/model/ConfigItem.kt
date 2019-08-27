package nz.co.trademe.konfigure.model

/**
 * The most basic representation of a config item. It has a key, a default value of type [T],
 * and an instance of [ConfigMetadata] for allowing consumers to append information to suit their needs.
 *
 * @param key The Config key
 * @param defaultValue The default value to use when retrieving the value of this item
 * @param metadata An object containing additional information optionally provided by the consumer
 */
data class ConfigItem<T>(
    val key: String,
    val defaultValue: T,
    val metadata: ConfigMetadata
)