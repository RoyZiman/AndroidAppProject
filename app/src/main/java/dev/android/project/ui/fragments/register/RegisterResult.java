package dev.android.project.ui.fragments.register;

import androidx.annotation.Nullable;

import dev.android.project.data.model.User;

public class RegisterResult
{
    @Nullable
    private User _success;
    @Nullable
    private String _error;

    RegisterResult(@Nullable String error)
    {
        _error = error;
    }

    RegisterResult(@Nullable User success)
    {
        _success = success;
    }

    @Nullable
    User getSuccess()
    {
        return _success;
    }

    @Nullable
    String getError()
    {
        return _error;
    }
}
