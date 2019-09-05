package nz.co.trademe.konfigure.sample.config.subconfig

import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.SubConfig
import nz.co.trademe.konfigure.android.extensions.config
import nz.co.trademe.konfigure.sample.config.subconfig.MoreNestedSubconfig

class TestSubConfig(parent: ConfigRegistry): SubConfig(parent) {
    override val group: String?
        get() = "Level 1"

    var test2: Boolean by config(
        key = "testBool",
        defaultValue = false,
        title = "Test Boolean",
        description = "This is a test boolean"
    )

    val nestedSubconfig = MoreNestedSubconfig(parent = this)
}