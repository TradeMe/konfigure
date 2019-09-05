# Konfigure-Android
Konfigure Android contains useful Android utilities. 

## SharedPreferences-backed `OverrideHandler`
Konfigure Android provides a simple `OverrideHandler` implementation backed by a dedicated `SharedPreferences` file. This allows for config overrides
to be persisted between app restarts. To use it, simply pass in your application context to the primary constructor. You can also optionally specify 
the name of the shared preference file.

```kotlin
class AppConfig(appContext: Context): Config(
	configSources = listOf(LocalSource),
	overrideHandler = SharedPreferencesOverrideHandler(appContext))
```

## Config UI
Konfigure Android provides a UI for overriding config values. The most simple way of accessing this UI is through using the supplied `ConfigActivity`:
```kotlin
ConfigActivity.start(activity)
```

If you want to customise the functionality of the Config UI, you have a couple of options

### Subclassing `ConfigActivity`
`ConfigActivity` has been provided as an `open class`. This allows subclassing for cases where it provides most things you need,
but you want to tack on extra little bits of functionality. An example of this can be found as the restart sample in the konfigure-sample module.

### Ditch the Activity, use ConfigView
Konfigure Android exposes another component for you to use, which is utilised by `ConfigActivity` under the hood. The modifiable list of config items is encapsulated within
`ConfigView` - a custom RecyclerView subclass which applied presentation logic. This can be used wherever all Views can be used - one use case might be embedding within a `Fragment`.

To make full use of `ConfigView`, we expose two pieces of functionality.

#### Config searching
Config searching is accessible through the `ConfigView#search` function. Example usage might be something like:
```kotlin
searchEditText.onTextChanged { searchTerm -> 
	configView.search(searchTerm)
}
```

#### Config Filters
`ConfigView` also exposes a concept of config filtering. This allows you to tell the view which config items you want to be displayed, based on a predicate you define. This can be 
particularly useful when you extend config items with metadata, allowing you to look for certain metadata or information contained within that metadata, and filter your config items accordingly. 

An example where filtering might be used is exposing config items to users:
```kotlin
val userVisibleFilter = object: ConfigView.Filter {
	override fun shouldKeepItem(item: ConfigItem<*>): Boolean {
		return item.metadata is UserVisibleMetadata
	}
}

configView.addFilter(userVisibleFilter)
```