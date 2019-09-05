# Konfigure-Firebase
Konfigure Firebase provides a simple implementation of a `FirebaseRemoteConfig` `ConfigSource`. 
By default it uses the instance of `FirebaseRemoteConfig` returned by `FirebaseRemoteConfig.getInstance()`. You can specify your own by using the optional `remoteConfig` parameter in the primary constructor.

## Usage
To use the `FirebaseRemoteConfigSource`, it's as easy as supplying it in the `Config` classes primary constructor
```kotlin
class AppConfig: Config(configSources = listOf(FirebaseRemoteConfigSource()))
```