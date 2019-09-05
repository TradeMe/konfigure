# Konfigure
This module contains the core of what makes up konfigure. Here you'll find the two classes you use, plus a bunch of things you might care about.

## Config and Subconfig
Your root level config implementation must extend from `Config`. A basic definition of this would be as such:
```kotlin
class AppConfig: Config(configSources = listOf(LocalSource)) {

	// The most simple config item, which uses the property name as a 
	// key and sensible non-null default values
	val someSimpleItem: Boolean by config()
}
```
If you find yourself with too many config items within a single class, or want to separate config items by feature, you can make use of the `SubConfig` class. To begin nesting items, first create your
subclass of `SubConfig`. You'll notice that `SubConfig` takes a `ConfigRegistry` parent in it's primary constructor. We'll pass that through. We also define a `group`, which is useful for some consumers:
```kotlin
class SomeSubConfig(parent: ConfigRegistry): SubConfig(parent) {
	override val group = "Some feature"
	
	val item: String by config()
}
```

We can then define config items as normal within our `SomeSubConfig` class. Finally, to register our subconfig implementation with the parent, we simply pass in this as the constructor from within the `Config` class:
```kotlin
class AppConfig: Config(configSources = listOf(LocalSource)) {

	// Register our subconfig. We can then access properties within it by chaining 
	// property access expressions - i.e: config.someSubConfig.item
	val someSubConfig = SomeSubConfig(parent = this)
}
```

## Config changes
Konfigure exposes changes made to config items (i.e overriding values) as a `Channel`. This enables us to easily model these changes as a hot stream of changes, whilst keeping this module kotlin-only.

To get access to this stream of changes, simply use the `changes` property. i.e:
```kotlin
val config = AppConfig()
...
launch {
	config.changes
		.toFlow()
		.collect { change -> ... }
}
```