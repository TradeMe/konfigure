package nz.co.trademe.konfigure.ui

import nz.co.trademe.konfigure.Config

/**
 * An interface to be implemented by your applications [android.app.Application] class
 */
interface ConfigProvider {

    /**
     * The config instance used to render the UI
     */
    val config: Config

}