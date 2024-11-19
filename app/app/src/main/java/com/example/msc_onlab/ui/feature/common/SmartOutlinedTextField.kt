package com.example.msc_onlab.ui.feature.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SmartOutlinedTextField(
    value: String,
    label: @Composable() (() -> Unit),
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String,
    singleLine: Boolean,
    maxLength: Int,
    readOnly: Boolean,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: @Composable() (() -> Unit)? = null,
    modifier: Modifier = Modifier,
){
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if(newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        label = {
            label()
        },
        isError = isError,
        singleLine = singleLine,
        supportingText = {
            if(isError){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
            else{
            Text(
                text = "${value.length}/$maxLength",
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
                }
        },
        trailingIcon = {
            if (isError) {
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = "Invalid value",
                    tint = MaterialTheme.colorScheme.error
                )
            } else if (value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Valid value",
                    tint = Color(0xFF296A48)
                )
            }
        },
        readOnly = readOnly,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        modifier = modifier.width(250.dp),
        placeholder = placeholder,
    )
}

@Preview(showBackground = true)
@Composable
private fun asd() {
    Column {

        SmartOutlinedTextField(
            value = "ASSD",
            label = { /*TODO*/ },
            onValueChange = {},
            isError = true,
            errorMessage = "Error message",
            singleLine = true,
            maxLength = 30,
            readOnly = false,
            enabled = true
        )

        SmartOutlinedTextField(
            value = "ASSD",
            label = { /*TODO*/ },
            onValueChange = {},
            isError = false,
            errorMessage = "Error message",
            singleLine = true,
            maxLength = 30,
            readOnly = false,
            enabled = true
        )
    }
}