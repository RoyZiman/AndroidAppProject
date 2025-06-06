package dev.android.project.ui.fragments.login;

import androidx.annotation.Nullable;

import dev.android.project.data.models.User;

/**
 * Represents the result of a login attempt, containing either a successful user or an error message.
 */
class LoginResult
{
    @Nullable
    private User _success;
    @Nullable
    private String _error;

    /**
     * Constructs a LoginResult with an error message.
     *
     * @param error The error message, or null if no error.
     */
    LoginResult(@Nullable String error)
    {
        _error = error;
    }

    /**
     * Constructs a LoginResult with a successful user.
     *
     * @param success The logged-in user, or null if login failed.
     */
    LoginResult(@Nullable User success)
    {
        _success = success;
    }

    /**
     * @return The successfully logged-in user, or null if login failed.
     */
    @Nullable
    User getSuccess()
    {
        return _success;
    }

    /**
     * @return The error message if login failed, or null if login was successful.
     */
    @Nullable
    String getError()
    {
        return _error;
    }
}