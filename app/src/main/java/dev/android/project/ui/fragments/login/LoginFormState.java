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

    LoginFormState(@Nullable Integer emailError, @Nullable Integer passwordError)
    {
        _emailError = emailError;
        _passwordError = passwordError;
        _isDataValid = false;
    }

    LoginFormState(boolean isDataValid)
    {
        _emailError = null;
        _passwordError = null;
        _isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError()
    {
        return _emailError;
    }

    @Nullable
    Integer getPasswordError()
    {
        return _passwordError;
    }

    boolean isDataValid()
    {
        return _isDataValid;
    }
}