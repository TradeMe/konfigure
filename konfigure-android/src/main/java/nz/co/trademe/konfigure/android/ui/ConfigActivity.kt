package nz.co.trademe.konfigure.android.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_config.*
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.view.ConfigView

/**
 * Basic activity hosting the [ConfigView]. This can be extended
 * to add basic functionality, like filtering of config items. Alternatively, [ConfigView]
 * can be used independently.
 *
 * To use, simply call [ConfigActivity.start]
 */
open class ConfigActivity: AppCompatActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
    }

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setTitle(R.string.configuration)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    @CallSuper
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_config, menu)

        val searchActionItem = menu.findItem(R.id.action_search)

        val searchView = searchActionItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                configView.search(query ?: "")
                hideSoftKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                configView.search(newText ?: "")
                return true
            }
        })

        return true
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideSoftKeyboard() {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            manager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    companion object {

        @JvmStatic
        fun start(activity: AppCompatActivity) =
            activity.startActivity(Intent(activity, ConfigActivity::class.java))
    }
}