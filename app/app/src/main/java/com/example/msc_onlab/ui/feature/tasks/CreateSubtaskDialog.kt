package com.example.msc_onlab.ui.feature.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.House
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.msc_onlab.helpers.Constants
import com.example.msc_onlab.helpers.DataFieldErrors
import com.example.msc_onlab.helpers.validateHouseholdName
import com.example.msc_onlab.ui.feature.common.SmartOutlinedTextField

@Composable
fun CreateSubtaskDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
){
    var title by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Create subtask",
                        fontWeight = FontWeight.Bold,
                    )
                    HorizontalDivider(modifier = Modifier.scale(0.9f))
                }

                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                SmartOutlinedTextField(
                    value = title,
                    label = {
                        Icon(
                            imageVector = Icons.Rounded.TaskAlt,
                            contentDescription = "Subtask title"
                        )
                    },
                    onValueChange = {
                        val error = validateHouseholdName(householdName = it, context = context)
                        isError = error !is DataFieldErrors.NoError
                        errorMessage = error.message

                        if (it.length <= Constants.MAX_HOUSEHOLD_NAME_LENGTH) {
                            title = it
                        }
                    },
                    isError = isError,
                    errorMessage = errorMessage,
                    singleLine = true,
                    maxLength = Constants.MAX_HOUSEHOLD_NAME_LENGTH,
                    readOnly = false,
                    enabled = true,
                    modifier = Modifier.focusRequester(focusRequester)
                )

                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }

                    TextButton(
                        onClick = {
                            onConfirmation(title)
                        },
                        enabled = !isError && title.isNotEmpty()
                    ) {
                        Text(text = "Create")
                    }
                }
            }
        }
    }
}
