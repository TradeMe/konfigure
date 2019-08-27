package nz.co.trademe.konfigure.sample.config

import nz.co.trademe.konfigure.SubConfig
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.sample.examples.restart.config

private const val GROUP = "Requires restart"

class RestartableSubconfig(parent: ConfigRegistry): SubConfig(parent) {
    override val group: String?
        get() = GROUP

    val darkMode by config(
        key = "dark_mode",
        title = "Dark Mode (Requires restart)",
        description = "Whether or not restarts are required",
        defaultValue = false,
        requiresRestart = true
    )
}