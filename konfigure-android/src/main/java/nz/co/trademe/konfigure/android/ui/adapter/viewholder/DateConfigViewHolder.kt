package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.android.databinding.ViewHolderDateBinding
import nz.co.trademe.konfigure.android.extensions.applicationConfig
import nz.co.trademe.konfigure.android.ui.ConfigActivity
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val ARG_DATE = "arg_date"
private const val TAG_EDIT_CONFIG = "tag_edit_config"

const val MED_DATE_FORMAT = "dd MMM yyyy"
const val TIME_FORMAT = "h:mma"

internal class DateConfigViewHolder(
    parent: ViewGroup,
) : BaseViewHolder<ViewHolderDateBinding>(parent.inflate(ViewHolderDateBinding::inflate)) {

    fun bind(model: ConfigAdapterModel.DateConfig) {
        with(binding) {
            titleTextView.text = model.metadata.title
            titleTextView.showAsModified(model.isModified)

            descriptionTextView.text = model.metadata.description

            dateValueButton.setOnClickListener {
                onEditDateClicked(it.context.applicationConfig, model)
            }

            timeValueButton.setOnClickListener {
                onEditTimeClicked(it.context.applicationConfig, model)
            }

            todaySetValueButton.setOnClickListener {
                val today = Date()
                setDateTextOnPickers(today)
                it.context.applicationConfig.setValueOf(model.item, Date::class, today)
            }

            setDateTextOnPickers(model.value)
        }
    }

    private fun setDateTextOnPickers(date: Date) {
        binding.dateValueButton.text = format(date, MED_DATE_FORMAT)
        binding.timeValueButton.text = format(date, TIME_FORMAT)
    }

    fun format(date: Date, format: String): String? {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.format(date)
    }

    private fun onEditDateClicked(config: Config, model: ConfigAdapterModel.DateConfig) {
        class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                val c = Calendar.getInstance()
                c.time = config.getValueOf(model.item, Date::class)
                val year = c[Calendar.YEAR]
                val month = c[Calendar.MONTH]
                val day = c[Calendar.DAY_OF_MONTH]
                return DatePickerDialog(requireActivity(), this, year, month, day)
            }

            override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
                val calendar = Calendar.getInstance()
                val date = config.getValueOf(model.item, Date::class)
                calendar.set(year, month, day, date.hours, date.minutes, date.seconds)
                config.setValueOf(model.item, Date::class, calendar.time)
            }
        }

        DatePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_DATE, model.value)
            }
        }.show((itemView.context as ConfigActivity).supportFragmentManager, TAG_EDIT_CONFIG)
    }

    private fun onEditTimeClicked(config: Config, model: ConfigAdapterModel.DateConfig) {
        class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                val c = Calendar.getInstance()
                c.time = config.getValueOf(model.item, Date::class)
                val hour = c[Calendar.HOUR]
                val minute = c[Calendar.MINUTE]
                return TimePickerDialog(requireActivity(), this, hour, minute, false)
            }

            override fun onTimeSet(picker: TimePicker?, hour: Int, minute: Int) {
                val date = config.getValueOf(model.item, Date::class)
                date.hours = hour
                date.minutes = minute
                config.setValueOf(model.item, Date::class, date)
            }
        }

        TimePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_DATE, model.value)
            }
        }.show((itemView.context as ConfigActivity).supportFragmentManager, TAG_EDIT_CONFIG)
    }
}
