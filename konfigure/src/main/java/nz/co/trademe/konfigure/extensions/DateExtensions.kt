package nz.co.trademe.konfigure.extensions

import java.text.SimpleDateFormat
import java.util.Locale

const val DATE_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

val configDateFormat = SimpleDateFormat(DATE_FORMAT_ISO_8601, Locale.ENGLISH)
