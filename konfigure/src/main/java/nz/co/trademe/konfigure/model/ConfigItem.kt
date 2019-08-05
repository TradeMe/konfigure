package nz.co.trademe.konfigure.model

data class ConfigItem<T>(
    val key: String,
    val defaultValue: T,
    val metadata: ConfigMetadata
)