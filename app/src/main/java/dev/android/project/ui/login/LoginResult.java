package  dev.android.project.ui.login;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import dev.android.project.data.model.User;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private User _success;
    @Nullable
    private String _error;

    LoginResult(@Nullable String error)
    {
        _error = error;
    }

    LoginResult(@Nullable User success)
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