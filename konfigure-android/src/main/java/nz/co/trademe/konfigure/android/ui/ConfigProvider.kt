package nz.co.trademe.konfigure.android.ui

import nz.co.trademe.konfigure.Config

/**
 * An interface to be implemented by your applications [android.app.Application] class
 */
interface ConfigProvider {

    /**
     * The config instance used by the application. This is used by the UI.
     */
    val config: Config

}