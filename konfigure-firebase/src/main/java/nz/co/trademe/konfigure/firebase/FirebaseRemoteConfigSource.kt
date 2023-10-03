package nz.co.trademe.konfigure.firebase

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.ConfigUpdateListenerRegistration
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import nz.co.trademe.konfigure.api.ConfigSource

/**
 * A simple FirebaseRemoteConfig source, for integration with konfigure.
 *
 * @param remoteConfig The [FirebaseRemoteConfig] instance to use for retrieving values from
 */
class FirebaseRemoteConfigSource @JvmOverloads constructor(
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
): ConfigSource {

    private var registration: ConfigUpdateListenerRegistration? = null

    /**
     * Registers a listener for config updates
     *
     * @param listener The listener to register for config updates
     */
    fun registerConfigUpdateListener(listener: FirebaseConfigUpdateListener) {
        registration = remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate().addOnCompleteListener {
                    listener.onConfigUpdate(configUpdate)
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                listener.onConfigError(error)
            }
        })
    }

    /** Unregisters the listener for config updates */
    fun unregisterConfigUpdateListener() {
        registration?.remove()
    }

    override val all: Map<String, String>
        get() = remoteConfig.all
            .mapValues { (_, value) -> value.asString() }
}
