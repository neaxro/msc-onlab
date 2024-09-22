package com.example.msc_onlab.helpers

import android.content.Context
import com.example.msc_onlab.R
import dagger.hilt.android.qualifiers.ApplicationContext

fun validateUsername(username: String, @ApplicationContext context: Context): DataFieldErrors {
    if(username.isEmpty()) return DataFieldErrors.UserNameError("Username is empty")
    if(username.length < Constants.MIN_USERNAME_LENGTH) return DataFieldErrors.UserNameError("Username is too short!")
    if(username.length > Constants.MAX_USERNAME_LENGTH) return DataFieldErrors.UserNameError("Username is too long!")
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
    if(password.isEmpty()) return DataFieldErrors.PasswordError("Password cannot be empty!")
    if(password.length < Constants.MIN_PASSWORD_LENGTH) return DataFieldErrors.PasswordError(context.getString(R.string.validator_error_password_is_too_short))
    if(password.length > Constants.MAX_PASSWORD_LENGTH) return DataFieldErrors.PasswordError(context.getString(R.string.validator_error_password_is_too_long))
    if(containsWhitespaces(password)) return DataFieldErrors.PasswordError(context.getString(R.string.validator_error_starts_or_ends_witch_whitespace))

    return DataFieldErrors.NoError
}

fun validateUserEmail(email: String, @ApplicationContext context: Context): DataFieldErrors {
    if(email.isEmpty()) return DataFieldErrors.EmailAddressError("Email cannot be empty!")
    if(email.length < Constants.MIN_EMAIL_LENGTH) return DataFieldErrors.EmailAddressError("Email is too short!")
    if(email.length > Constants.MAX_EMAIL_LENGTH) return DataFieldErrors.EmailAddressError("Email is too long!")
    if(!email.matches(Regex("^[A-Za-z0-9.]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))) return DataFieldErrors.EmailAddressError(context.getString(R.string.validator_error_invalid_format))

    return DataFieldErrors.NoError
}

fun validateFirstname(firstname: String, @ApplicationContext context: Context): DataFieldErrors {
    if(firstname.isEmpty()) return DataFieldErrors.FirstnameError("Firstname cannot be empty!")
    if(firstname.length < Constants.MIN_FIRSTNAME_LENGTH) return DataFieldErrors.FirstnameError("Firstname is too short!")
    if(firstname.length > Constants.MAX_FIRSTNAME_LENGTH) return DataFieldErrors.FirstnameError("Firstname is too long!")
    if(containsNumbers(firstname)) return DataFieldErrors.FirstnameError("Firstname cannot contain numbers!")
    if(containsSpecialCharacters(firstname)) return DataFieldErrors.FirstnameError("Firstname cannot contain special characters!")
    if(containsWhitespaces(firstname)) return DataFieldErrors.FirstnameError("Firstname cannot contain whitespace(s)!")

    return DataFieldErrors.NoError
}

fun validateLastname(lastname: String, @ApplicationContext context: Context): DataFieldErrors {
    if(lastname.isEmpty()) return DataFieldErrors.LastnameError("Lastname cannot be empty!")
    if(lastname.length < Constants.MIN_LASTNAME_LENGTH) return DataFieldErrors.LastnameError("Lastname is too short!")
    if(lastname.length > Constants.MAX_LASTNAME_LENGTH) return DataFieldErrors.LastnameError("Lastname is too long!")
    if(containsNumbers(lastname)) return DataFieldErrors.LastnameError("Lastname cannot contain numbers!")
    if(containsSpecialCharacters(lastname)) return DataFieldErrors.LastnameError("Lastname cannot contain special characters!")
    if(containsWhitespaces(lastname)) return DataFieldErrors.LastnameError("Lastname cannot contain whitespace(s)!")

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
    class EmailAddressError(msg: String): DataFieldErrors(message = msg)
    class FirstnameError(msg: String): DataFieldErrors(message = msg)
    class LastnameError(msg: String): DataFieldErrors(message = msg)

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
