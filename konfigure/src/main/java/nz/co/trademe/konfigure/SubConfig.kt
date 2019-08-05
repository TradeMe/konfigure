package nz.co.trademe.konfigure

import nz.co.trademe.konfigure.model.ConfigItem
import nz.co.trademe.konfigure.model.ConfigMetadata
import kotlin.properties.ReadWriteProperty

abstract class SubConfig(protected val configApi: Config.SubConfigApi) {

    /**
     * Main function to be used by extending classes to define various config parameters.
     */
    protected inline fun <reified T : Any> config(
        key: String,
        defaultValue: T,
        metadata: ConfigMetadata): ReadWriteProperty<Any, T> {
        val configItem = ConfigItem(key, defaultValue, metadata)
        configApi.updateConfig(configItem)
        return configApi.provideDelegate(configItem)
    }
}