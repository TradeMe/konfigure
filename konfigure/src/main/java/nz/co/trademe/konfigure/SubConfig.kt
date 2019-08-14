package nz.co.trademe.konfigure

import nz.co.trademe.konfigure.api.ConfigRegistry

/**
 * This class represents a subset of the overall list of  [Config]
 */
abstract class SubConfig(
    override val parent: ConfigRegistry
) : ConfigRegistry by parent {

    abstract override val group: String?
}