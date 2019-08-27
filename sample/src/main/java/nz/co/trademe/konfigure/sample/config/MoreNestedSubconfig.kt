package nz.co.trademe.konfigure.sample.config

import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.SubConfig
import nz.co.trademe.konfigure.android.extensions.config

class MoreNestedSubconfig(parent: ConfigRegistry): SubConfig(parent) {
    override val group: String?
        get() = "Level 2"

    var test2: Boolean by config(
        key = "testBool2",
        defaultValue = false,
        title = "Nested Test Boolean",
        description = "This is a test boolean"
    )
}