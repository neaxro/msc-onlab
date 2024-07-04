package ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun MaxLengthOutlinedTextField(
    value: String,
    label: @Composable() (() -> Unit),
    onValueChange: (String) -> Unit,
    isError: Boolean,
    singleLine: Boolean,
    maxLength: Int,
    readOnly: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
){
    /*
    TextField(

    )

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            label()
        },
        isError = isError,
        singleLine = singleLine,
        supportingText = {
            Text(
                text = stringResource(
                    R.string.composable_min_max_length_indicator,
                    value.length,
                    maxLength
                ),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        },
        trailingIcon = {
            if (isError) {
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = stringResource(R.string.composable_invalid_value),
                    tint = Color.Red
                )
            } else if (value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = stringResource(R.string.composable_valid_value),
                    tint = Color.Green
                )
            }
        },
        readOnly = readOnly,
        enabled = enabled,
        modifier = modifier
    )*/
}
