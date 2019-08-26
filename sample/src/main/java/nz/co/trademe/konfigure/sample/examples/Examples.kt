package nz.co.trademe.konfigure.sample.examples

import androidx.appcompat.app.AppCompatActivity
import nz.co.trademe.konfigure.sample.examples.basic.BasicExample
import nz.co.trademe.konfigure.sample.examples.restart.RestartExample

interface Example {
    val title: String
    val description: String

    fun onClick(activity: AppCompatActivity)
}

val AllExamples = listOf(
    BasicExample,
    RestartExample
)



