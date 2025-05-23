package dev.android.project.ui.fragments.register;

import androidx.annotation.Nullable;

public class RegisterFormState
{
    @Nullable
    private final Integer _displayNameError;
    @Nullable
    private final Integer _emailError;
    @Nullable
    private final Integer _passwordError;
    private final boolean _isDataValid;

    public RegisterFormState(@Nullable Integer displayNameError,
                             @Nullable Integer emailError,
                             @Nullable Integer passwordError)
    {
        _displayNameError = displayNameError;
        _emailError = emailError;
        _passwordError = passwordError;
        _isDataValid = false;
    }

    public RegisterFormState(boolean isDataValid)
    {
        _displayNameError = null;
        _emailError = null;
        _passwordError = null;
        _isDataValid = isDataValid;
    }


    @Nullable
    Integer getDisplayNameError()
    {
        return _displayNameError;
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
