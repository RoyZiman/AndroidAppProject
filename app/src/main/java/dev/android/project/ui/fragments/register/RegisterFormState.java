package dev.android.project.ui.fragments.register;

import androidx.annotation.Nullable;

/**
 * Represents the state of the registration form, including validation errors and overall validity.
 */
public class RegisterFormState
{
    @Nullable
    private final Integer _displayNameError;
    @Nullable
    private final Integer _emailError;
    @Nullable
    private final Integer _passwordError;
    private final boolean _isDataValid;

    /**
     * Constructs a RegisterFormState with specific field errors.
     *
     * @param displayNameError Error resource id for display name, or null if valid.
     * @param emailError Error resource id for email, or null if valid.
     * @param passwordError Error resource id for password, or null if valid.
     */
    public RegisterFormState(@Nullable Integer displayNameError,
                             @Nullable Integer emailError,
                             @Nullable Integer passwordError)
    {
        _displayNameError = displayNameError;
        _emailError = emailError;
        _passwordError = passwordError;
        _isDataValid = false;
    }

    /**
     * Constructs a RegisterFormState with a validity flag and no errors.
     *
     * @param isDataValid True if the form data is valid, false otherwise.
     */
    public RegisterFormState(boolean isDataValid)
    {
        _displayNameError = null;
        _emailError = null;
        _passwordError = null;
        _isDataValid = isDataValid;
    }

    /**
     * @return Error resource id for display name, or null if valid.
     */
    @Nullable
    Integer getDisplayNameError()
    {
        return _displayNameError;
    }

    /**
     * @return Error resource id for email, or null if valid.
     */
    @Nullable
    Integer getEmailError()
    {
        return _emailError;
    }

    /**
     * @return Error resource id for password, or null if valid.
     */
    @Nullable
    Integer getPasswordError()
    {
        return _passwordError;
    }

    /**
     * @return True if the form data is valid, false otherwise.
     */
    boolean isDataValid()
    {
        return _isDataValid;
    }
}
