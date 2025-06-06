package dev.android.project.ui.fragments.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState
{
    @Nullable
    private final Integer _emailError;
    @Nullable
    private final Integer _passwordError;
    private final boolean _isDataValid;

    /**
     * Constructs a new LoginFormState with specific error messages for each field.
     *
     * @param emailError the error message resource id for the email field, or null if no error.
     * @param passwordError the error message resource id for the password field, or null if no error.
     */
    LoginFormState(@Nullable Integer emailError, @Nullable Integer passwordError)
    {
        _emailError = emailError;
        _passwordError = passwordError;
        _isDataValid = false;
    }

    /**
     * constructs a new LoginFormState with a validity flag and no errors.
     *
     * @param isDataValid true if the form data is valid, false otherwise.
     */
    LoginFormState(boolean isDataValid)
    {
        _emailError = null;
        _passwordError = null;
        _isDataValid = isDataValid;
    }

    /**
     * @return the error message resource id for the email field, or null if no error
     */
    @Nullable
    Integer getEmailError()
    {
        return _emailError;
    }

    /**
     * @return the error message resource id for the password field, or null if no error
     */
    @Nullable
    Integer getPasswordError()
    {
        return _passwordError;
    }

    /**
     * @return true if the form data is valid
     */
    boolean isDataValid()
    {
        return _isDataValid;
    }
}