package nz.co.trademe.konfigure.firebase

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException

/**
 * A listener for config updates from FirebaseRemoteConfig
 */
interface FirebaseConfigUpdateListener {
    fun onConfigUpdate(configUpdate: ConfigUpdate)
    fun onConfigError(exception: FirebaseRemoteConfigException)
}
