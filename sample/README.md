# Sample Project
This sample project contains three usage example of Konfigure. All application config items are declared in [AppConfig.kt](sample/src/main/java/nz/co/trademe/konfigure/sample/config/AppConfig.kt) 
and registered subconfigs.

## [Basic Example](sample/src/main/java/nz/co/trademe/konfigure/sample/examples/basic)
The basic example shows the most simple usage of Konfigure, i.e launching `ConfigActivity`

## [Restart Example](sample/src/main/java/nz/co/trademe/konfigure/sample/examples/restart)
This example shows how we can extend `ConfigActivity` to add separate functionality - in this case, the ability to observe config changes, and track whether or not a restart is required
based on metadata attached to config items. 

## [Filtering Example](sample/src/main/java/nz/co/trademe/konfigure/sample/examples/filtering)
This example shows how we can extend `ConfigActivity` to add filter config items. We've added simple filtering, where certain config items are user visible. 

