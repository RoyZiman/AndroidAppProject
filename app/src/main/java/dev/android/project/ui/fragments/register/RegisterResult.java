package dev.android.project.ui.fragments.register;

import androidx.annotation.Nullable;

import dev.android.project.data.models.User;

/**
 * Represents the result of a registration attempt, containing either a successful user or an error message.
 */
public class RegisterResult
{
    @Nullable
    private User _success;
    @Nullable
    private String _error;

    /**
     * Constructs a RegisterResult with an error message.
     *
     * @param error The error message, or null if no error.
     */
    RegisterResult(@Nullable String error)
    {
        _error = error;
    }

    /**
     * Constructs a RegisterResult with a successful user.
     *
     * @param success The registered user, or null if registration failed.
     */
    RegisterResult(@Nullable User success)
    {
        _success = success;
    }

    /**
     * @return The successfully registered user, or null if registration failed.
     */
    @Nullable
    User getSuccess()
    {
        return _success;
    }

    /**
     * @return The error message if registration failed, or null if registration was successful.
     */
    @Nullable
    String getError()
    {
        return _error;
    }
}
