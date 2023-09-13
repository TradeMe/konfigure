package nz.co.trademe.konfigure.firebase

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException

interface FirebaseConfigUpdateListener {
    fun onConfigUpdate(configUpdate: ConfigUpdate)
    fun onConfigError(exception: FirebaseRemoteConfigException)
}
