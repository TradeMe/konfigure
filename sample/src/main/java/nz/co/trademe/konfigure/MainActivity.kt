package nz.co.trademe.konfigure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import nz.co.trademe.konfigure.ui.ConfigActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configButton.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }
    }
}
