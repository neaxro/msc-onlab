package com.example.msc_onlab.ui.feature.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.msc_onlab.ui.theme.MsconlabTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    value: String,
    label: @Composable() (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    readOnly: Boolean,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = FutureOrPresentSelectableDates
    )
    val selectedDate: String = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: value

    LaunchedEffect(selectedDate) {
        onValueChange(selectedDate)
    }

    Box(
        modifier = modifier.width(250.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {  },
            label = label,
            readOnly = true,
            trailingIcon = {
                if(!readOnly){
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            isError = isError
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Preview(showBackground = true)
@Composable
fun DatePickerDockedPreview() {

    var date by remember{ mutableStateOf("") }

    MsconlabTheme {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(10.dp))

            Text(text = date)

            Spacer(modifier = Modifier.padding(10.dp))

            DatePickerDocked(
                value = date,
                onValueChange = { date = it },
                readOnly = true
            )

            Spacer(modifier = Modifier.padding(10.dp))

            DatePickerDocked(
                value = date,
                onValueChange = { date = it },
                readOnly = false
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object FutureOrPresentSelectableDates: SelectableDates {
    private val now = LocalDate.now()
    private val dayStart = now.atTime(0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= dayStart
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= now.year
    }
}