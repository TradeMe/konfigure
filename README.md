# Konfigure

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [ ![Download](https://api.bintray.com/packages/trademe/Konfigure/konfigure/images/download.svg) ](https://bintray.com/trademe/Konfigure/konfigure/_latestVersion)

Kotlin Property Delegation-based application configuration library. Tailored for Android, but ready for multiplatform.
## Download

```groovy
// Base module
implementation "nz.co.trademe.konfigure:konfigure:${current.version}" 

// Android module, including UI
implementation "nz.co.trademe.konfigure:konfigure-android:${current.version}" 

// Extra: Firebase Remote Config
implementation "nz.co.trademe.konfigure:konfigure-firebase:${current.version}"
```
## Usage
1. Create a config source, or optionally use `FirebaseRemoteConfigSource` from the `konfigure-firebase` module
```kotlin
object LocalSource: ConfigSource {

    override val all: Map<String, String>
        get() = TODO("Provide some config key-value pairs")
}
```
   
2. Create a config subclass 
```kotlin
class AppConfig: Config(configSources = listOf(LocalSource))
```

3. Add some configuration items
```kotlin
class AppConfig: Config(configSources = listOf(LocalSource)) {

    // The most simple config item, which uses the property name as a 
    // key and sensible non-null default values
    val someSimpleItem: Boolean by config()

    // Add a simple config item 
    val someItem: Boolean by config(
        key = "item_key",
        defaultValue = false
    )

    // OR, using `konfigure-android`, add a displayable config item
    val someEditableItem: Boolean by config(
        key = "item_key_2",
        defaultValue = false,
        title = "Something",
        description = "Some editable config item",
        group = "Some group"
    )
}
```

4. Use it!

```kotlin
val config = AppConfig()

// Use the property! This will check your config sources, look for overrides, and if it doesn't 
// find anything will use the defaultValue parameter
if (config.someItem) {
    TODO("Do something cool")
}
```

Konfigure supports much more advanced usages however. By adding custom metadata, you can customise the behaviour of konfigure
to your hearts content. For more examples, check out the [sample](/sample).

## Modules
Konfigure contains a number of modules, which are useful for different things. Here's a quick run down of what they all do - more information can be found within these modules.

### [`konfigure`](/konfigure)
This is the base module for Konfigure, which contains the core logic. Usage is as describe in the usage section of this README.

### [`konfigure-android`](/konfigure-android)
This module contains Android-specific utilities, as well as a UI component which allows you to change the value of the config items within the app. In here you'll find things like `SharedPreferencesOverrideHandler` which gives you easily persistable overrides, as well as `ConfigActivity` and `ConfigView` for presenting overrides.

### [`konfigure-firebase`](/konfigure-firebase)
This module contains a pre-build Firebase Remote Config source. Eventually this could be made multiplatform, as the Firebase SDK can be found on most platforms Kotlin/Multiplatform supports.


## Contributing 
We love contributions, but make sure to checkout [CONTRIBUTING.MD](/CONTRIBUTING.MD) first!
