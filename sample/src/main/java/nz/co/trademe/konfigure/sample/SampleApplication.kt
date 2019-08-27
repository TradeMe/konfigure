package nz.co.trademe.konfigure.sample

import android.app.Application
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.sample.config.AppConfig
import nz.co.trademe.konfigure.android.ui.ConfigProvider

class SampleApplication: Application(), ConfigProvider {

    override val config: Config by lazy {
        AppConfig(this)
    }
}