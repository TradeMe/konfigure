package nz.co.trademe.konfigure

import android.app.Application
import nz.co.trademe.konfigure.config.AppConfig
import nz.co.trademe.konfigure.ui.ConfigProvider

class SampleApplication: Application(), ConfigProvider {

    override val config: Config by lazy {
        AppConfig(this)
    }
}