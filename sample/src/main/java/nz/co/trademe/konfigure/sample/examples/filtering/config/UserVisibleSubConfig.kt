package nz.co.trademe.konfigure.sample.examples.filtering.config

import nz.co.trademe.konfigure.SubConfig
import nz.co.trademe.konfigure.api.ConfigRegistry

class UserVisibleSubConfig(parent: ConfigRegistry): SubConfig(parent) {

    override val group: String?
        get() = "User Visible items"

    /**
     * Here we use our [userVisibleConfig] extension function to apply [UserVisibleMetadata] to our config
     */
    var userVisibleItem: Boolean by userVisibleConfig(
        key = "some_user_visible_item",
        title = "User Visible Item",
        description = "It's mandatory to fill out the description for user visible config items"
    )
}