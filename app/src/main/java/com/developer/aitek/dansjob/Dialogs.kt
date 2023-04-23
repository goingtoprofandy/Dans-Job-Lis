package com.developer.aitek.dansjob

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.text.format.DateFormat
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

// Generated Custom List Dialogs
fun customDialog(activity: Activity, items: List<String>,
                 dialogView: (DialogInterface, String) -> Unit) {

    val builder = AlertDialog.Builder(activity)
    val adapter =  ArrayAdapter<String>(activity, android.R.layout.select_dialog_singlechoice)
    adapter.addAll(*items.toTypedArray())
    builder.setAdapter(adapter) { dialog, which ->
        dialogView(dialog, adapter.getItem(which)!!)
    }
    builder.show()
}

// Message Dialog
fun dialogMessage(activity: Activity,
                  message: String,
                  dialogView: (DialogInterface) -> Unit) {

    val builder = AlertDialog.Builder(activity)
    builder.setMessage(message).setPositiveButton(
        "OK"
    ) { dialog, _ ->
        dialogView(dialog)
    }
    builder.show()
}

// Custom Dialog With View
fun customDialog(activity: Activity, layout: Int,
                 onSetView: (View) -> Unit,
                 dialogView: (DialogInterface, view: View) -> Unit) {
    val builder = AlertDialog.Builder(activity)
    // Get the layout inflater
    val inflater = activity.layoutInflater;
    val view = inflater.inflate(layout, null)

    onSetView(view)

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    builder.setView(view)
        // Add action buttons
        .setPositiveButton("Simpan"
        ) { dialog, _ ->
            dialogView(dialog, view)
        }
    builder.create()
        .show()
}

// Custom Dialog With List Index
fun customDialog(activity: Activity, items: List<String>,
                 dialogView: (DialogInterface, String, Int) -> Unit) {

    val builder = AlertDialog.Builder(activity)
    val adapter =  ArrayAdapter<String>(activity, android.R.layout.select_dialog_singlechoice)
    adapter.addAll(*items.toTypedArray())
    builder.setAdapter(adapter) { dialog, which ->
        dialogView(dialog, adapter.getItem(which)!!, which)
    }
    builder.show()
}

// Generated DatePicker Dialogs
fun datePicker(activity: Activity, isMin: Boolean, onSetDate: (String) -> Unit) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(activity, { _, currYear, monthOfYear, dayOfMonth ->
        val newMonth = if((monthOfYear + 1) < 10) "0${(monthOfYear + 1)}" else "${(monthOfYear + 1)}"
        val newDay = if(dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
        onSetDate("$currYear-${newMonth}-${newDay}")
    }, year, month, day)

    val datePicker = dpd.datePicker
    if (isMin)
    datePicker.minDate = c.timeInMillis
    else datePicker.maxDate = c.timeInMillis

    dpd.show()
}

// Generated TimePicker Dialogs
fun timePicker(activity: Activity, onSetTime: (String) -> Unit) {
    val c = Calendar.getInstance()
    val hour = c.get(Calendar.HOUR_OF_DAY)
    val minute = c.get(Calendar.MINUTE)

    TimePickerDialog(activity, { _, hourOfDay, minute ->
        val newHour = if(hourOfDay < 10) "0${hourOfDay}" else "$hourOfDay"
        val newMinute = if(minute < 10) "0${minute}" else "$minute"

        onSetTime(StringBuilder()
            .append(newHour).append(":")
            .append(newMinute).toString())
    }, hour, minute, DateFormat.is24HourFormat(activity)).show()
}

fun showBottomSheetDialog(activity: Activity, layout: Int, onLoadView: (BottomSheetDialog) -> Unit) {
    val dialog = BottomSheetDialog(activity)
    dialog.setContentView(layout)
    onLoadView(dialog)
    dialog.show()
}