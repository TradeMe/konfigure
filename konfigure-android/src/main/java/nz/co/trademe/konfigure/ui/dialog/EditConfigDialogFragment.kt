package nz.co.trademe.konfigure.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.edit_config_dialog.view.*
import nz.co.trademe.konfigure.R
import nz.co.trademe.konfigure.extensions.applicationConfig
import nz.co.trademe.konfigure.model.ConfigItem

private const val ARG_CONFIG_ITEM_KEY = "arg_config_item_key"
private const val ARG_EDIT_TYPE_ORDINAL = "arg_edit_type_ordinal"
private const val ARG_CURRENT_VALUE = "arg_current_value"
private const val TAG_EDIT_CONFIG = "tag_edit_config"

internal class EditConfigDialogFragment : DialogFragment() {

    private lateinit var contentView: View

    private val configItem: ConfigItem<*>
        get() = arguments?.getString(ARG_CONFIG_ITEM_KEY)?.let { key ->
            context?.applicationContext?.applicationConfig?.configItems?.find { it.key == key }
        } ?: throw IllegalArgumentException("Missing config item key argument")

    private val editType: EditConfigType
        get() {
            val ordinal = arguments?.getInt(ARG_EDIT_TYPE_ORDINAL)
                ?: throw IllegalArgumentException("Missing edit config ordinal argument")
            return EditConfigType.values()[ordinal]
        }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create our custom view with the edit text.
        val layoutInflater = LayoutInflater.from(context)
        contentView = layoutInflater.inflate(R.layout.edit_config_dialog, null, false)

        // Handle the tick in the on-screen keyboard.
        contentView.configTextInputEditText.setOnEditorActionListener { _: TextView, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onOkayClicked()
                true
            } else {
                false
            }
        }

        contentView.configTextInputEditText.inputType = when (editType) {
            EditConfigType.STRING -> InputType.TYPE_CLASS_TEXT
            EditConfigType.NUMBER -> InputType.TYPE_CLASS_NUMBER
        }

        // Set the text on the input field
        arguments?.getString(ARG_CURRENT_VALUE)?.let {
            contentView.configTextInputEditText.append(it)
        }

        // Build the dialog.
        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle(R.string.edit_config)
            .setView(contentView)
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    override fun onResume() {
        super.onResume()

        // Override the OK button click handler.
        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            onOkayClicked()
        }
    }

    /**
     * Called when the user clicks the OK button.
     */
    private fun onOkayClicked() {
        val configValue = contentView.configTextInputEditText.text?.trim().toString()

        // Don't allow updating to an empty String
        if (configValue.isEmpty()) {
            contentView.configTextInputLayout.error = getString(R.string.config_blank_error)
            return
        }

        // The following is a bit of a shame - it's not possible to define an inline function in an
        // interface, but due to the type-checking implemented by the Config library, this callback
        // must be inline such that the library receives full type information.
        @Suppress("UNCHECKED_CAST")
        when (editType) {
            EditConfigType.STRING ->
                contentView.context.applicationConfig.setValueOf(configItem as ConfigItem<String>, String::class, configValue)
            EditConfigType.NUMBER ->
                contentView.context.applicationConfig.setValueOf(configItem as ConfigItem<Long>, Long::class, configValue.toLong())
        }

        // Dismiss the dialog.
        dialog?.dismiss()
    }

    companion object {

        @JvmStatic
        fun start(configItem: ConfigItem<*>, editConfigType: EditConfigType, currentValue: String, fragmentManager: FragmentManager) {
            EditConfigDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CONFIG_ITEM_KEY, configItem.key)
                    putInt(ARG_EDIT_TYPE_ORDINAL, editConfigType.ordinal)
                    putString(ARG_CURRENT_VALUE, currentValue)
                }
            }.show(fragmentManager, TAG_EDIT_CONFIG)
        }
    }
}