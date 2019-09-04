package nz.co.trademe.konfigure.api

import kotlin.properties.ReadWriteProperty

interface ConfigDelegate<T: Any>: ReadWriteProperty<ConfigRegistry, T>