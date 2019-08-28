package nz.co.trademe.konfigure

import nz.co.trademe.konfigure.api.ConfigRegistry

/**
 * This class represents a subset of the overall list of config items.
 * Config registration is mostly delegated to the parent [ConfigRegistry]. This
 * class also enforces the inclusion of a group.
 *
 * @param parent The parent [ConfigRegistry] to delegate to
 */
abstract class SubConfig(
    override val parent: ConfigRegistry
) : ConfigRegistry by parent {

    abstract override val group: String?
}