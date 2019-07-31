# Konfigure - Kotlin Configuration Library
 
### Description
Internally at Trade Me we've been using a property delegation based configuration library for the past 9 months, with great success. This internal library has allowed our developers to quickly and easily write abstractions over Firebase Remote Config entries, allowing the config entries to be overwritten by developers and testers via custom UI. This previously has been relatively opinionated, with `FirebaseRemoteConfig`, `LiveData` etc. baked in to the main class. This constrains the library somewhat, as it forces consumers to use constructs that may not match their project. This re-write of the library is meant to address these issues, making the library more flexible and generic. 

### Architecture
The biggest ambition with this project is to make the core of the library solely dependent on Kotlin stdlib and multiplatform tools. This will allow it to be used in future as a multi-platform library is needed by projects. 
* `LiveData` will be replaced with coroutine-based streams for asynchronous stream abstractions. `ConflatedBroadcastChannel` provides similar functionality to `LiveData`, and will be used in place of it in the main class. 
* `FirebaseRemoteConfig` will be replaced with an abstraction, essentially a typed `AbstractMap` which is keyed with a `String` and returns `String`s. This abstraction will be known as a `ConfigSource`. One improvement over the existing setup is that I plan on adding the ability for _multiple_ config sources. This could be useful for cases where you have `FirebaseRemoteConfig`, and existing shared preferences which you want to expose etc.
* Overridden config items had previously been stored in a named `SharedPreferences` file. This will be extracted into a special `ConfigSource` which implements an `OverrideHandler` interface (name subject to change). This essentially turns that source into a mutable source, exposing setter-like methods. Only one of these will be allowed. If no `OverrideHandler` is specified, the UI component of this work will fail. 

To maintain compatibility with the Trade Me applications, we need to ensure no functionality is removed which is relied on. Restart tracking and exposure of changes via `LiveData` is a must. It must also still contain a UI component, which is used by our applications. These components will be extracted into separate modules which add functionality to the base of the library. This allows consumers to not use Android-specific components of the work if not needed, and leaves it open for extension to other platforms if anyone's interested in building these. 

The module setup is planned to be as such:
```
konfigure/
├── sample/
├── konfigure/
├── konfigure-android/
├── konfigure-android-extensions/
└── konfigure-firebase/
```
* `konfigure` - this is the base, Kotlin-only module. This contains the delegates, streams for changes etc. 
* `konfigure-android` - this will contain the UI, as well as including the `konfigure-android-extensions` modules as an `api` dependency such that it's available to consumers. This will mean one `implementation` per Android project, as opposed to having to include all modules. It will also contain a `SharedPreferences` backed `OverrideHandler`.
* `konfigure-android-extensions` - this will contain only the bare essentials needed to make `konfigure` more Android-friendly. This includes things like `LiveData` extensions.
* `konfigure-firebase` - this will be set up as a `kotlin-multiplatform` module, however will only have support for Android deployment for the time being. This will add a `FirebaseConfigSource`, which will call out to appropriate platform-specific Firebase SDKs. By making this multiplatform ready, we open the door for future work to make it iOS compatible via Kotlin/Native.

### Libraries
* Kotlin Standard Library
* KotlinX Coroutines
* Kotlin Android Extensions (in separate module)
* Firebase Remote Config (in separate module)
* Lifecycle Extensions (LiveData)
 
### Review Volunteers