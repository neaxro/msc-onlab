package com.example.msc_onlab.helpers

import android.content.Context
import com.example.msc_onlab.R
import dagger.hilt.android.qualifiers.ApplicationContext

fun validateUserUsername(username: String, @ApplicationContext context: Context): DataFieldErrors {
    if(username.isEmpty()) return DataFieldErrors.UserNameError("Username is empty")
    if(username.length < Constants.MIN_USER_NAME_LENGTH) return DataFieldErrors.UserNameError("Username is too short!")
    if(username.length > Constants.MAX_USER_NAME_LENGTH) return DataFieldErrors.UserNameError("Username is too long!")
    if(containsWhitespaces(username)) return DataFieldErrors.UserNameError(context.getString(R.string.validator_error_starts_or_ends_witch_whitespace))
    if(containsCapitalCharacters(username)) return DataFieldErrors.UserNameError(context.getString(R.string.validator_error_contains_capital_letters))
    if(containsSpecialCharacters(username)) return DataFieldErrors.UserNameError(context.getString(R.string.validator_error_contains_special_characters))

    return DataFieldErrors.NoError
}

fun validatePasswordsMatch(password: String, passwordChange: String, @ApplicationContext context: Context): DataFieldErrors{
    if(password != passwordChange) return DataFieldErrors.PasswordMismatchError(context.getString(R.string.validator_error_passwords_do_not_match))

    return DataFieldErrors.NoError
}

fun validateUserPassword(password: String, @ApplicationContext context: Context): DataFieldErrors {
    if(password.isEmpty()) return DataFieldErrors.PasswordError("Password is empty")
    if(password.length < Constants.MIN_USER_PASSWORD_LENGTH) return DataFieldErrors.PasswordError(context.getString(R.string.validator_error_password_is_too_short))
    if(password.length > Constants.MAX_USER_PASSWORD_LENGTH) return DataFieldErrors.PasswordError(context.getString(R.string.validator_error_password_is_too_long))
    if(containsWhitespaces(password)) return DataFieldErrors.PasswordError(context.getString(R.string.validator_error_starts_or_ends_witch_whitespace))

    return DataFieldErrors.NoError
}

private fun containsSpecialCharacters(text: String): Boolean {
    return text.matches(Regex("\\w*[^\\w\\s]+\\w*"))
}

private fun containsNumbers(text: String): Boolean {
    return text.matches(Regex(".*[0-9].*"))
}

private fun containsWhitespaces(text: String): Boolean {
    return text.matches(Regex(".*\\s.*"))
}

private fun containsCapitalCharacters(text: String): Boolean {
    return text.matches(Regex(".*[A-Z].*"))
}

private fun startsOrEnsWithWhitespace(text: String): Boolean {
    return text.matches(Regex("^\\s.*|.*\\s\$"))
}

sealed class DataFieldErrors(val message: String){
    object NoError: DataFieldErrors(message = "No Error")
    class UserNameError(msg: String): DataFieldErrors(message = msg)
    class PasswordError(msg: String): DataFieldErrors(message = msg)
    class PasswordMismatchError(msg: String): DataFieldErrors(message = msg)
}

fun DataFieldErrors.or(error: DataFieldErrors): DataFieldErrors {
    if(this !is DataFieldErrors.NoError) return this
    return error
}

fun Collection<DataFieldErrors>.containsType(errorType: Class<out DataFieldErrors>): Boolean {
    this.forEach { error ->
        if(errorType.isInstance(error)){
            return true
        }
    }

    return false
}

fun Collection<DataFieldErrors>.getErrorMessage(errorType: Class<out DataFieldErrors>): String {
    this.forEach { error ->
        if(errorType.isInstance(error)){
            return error.message
        }
    }

    return ""
}

fun DataFieldErrors.isError(): Boolean{
    return this !is DataFieldErrors.NoError
}
