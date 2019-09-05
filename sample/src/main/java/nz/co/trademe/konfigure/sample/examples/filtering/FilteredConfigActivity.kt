package nz.co.trademe.konfigure.sample.examples.filtering

import android.os.Bundle
import nz.co.trademe.konfigure.android.ui.ConfigActivity
import nz.co.trademe.konfigure.android.ui.view.ConfigView
import nz.co.trademe.konfigure.model.ConfigItem
import nz.co.trademe.konfigure.sample.examples.filtering.config.UserVisibleMetadata

class FilteredConfigActivity: ConfigActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Filter all items which are user visible
        val filter = object: ConfigView.Filter {
            override fun shouldKeepItem(item: ConfigItem<*>): Boolean {
                return item.metadata is UserVisibleMetadata
            }
        }

        configurationView.addFilter(filter)
    }
}