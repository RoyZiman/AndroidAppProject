package dev.android.project.ui.fragments.register;

import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import dev.android.project.R;
import dev.android.project.data.model.User;
import dev.android.project.data.providers.DBUsersManager;

public class RegisterViewModel extends ViewModel
{
    private final MutableLiveData<RegisterFormState> _registerFormState = new MutableLiveData<>();
    private final MutableLiveData<RegisterResult> _registerResult = new MutableLiveData<>();
    private final FirebaseAuth AUTH;

    RegisterViewModel()
    {
        AUTH = FirebaseAuth.getInstance();
    }

    MutableLiveData<RegisterFormState> getRegisterFormState()
    {
        return _registerFormState;
    }

    MutableLiveData<RegisterResult> getRegisterResult()
    {
        return _registerResult;
    }

    public void register(String displayName, String email, String password)
    {
        AUTH.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                authResult.getUser()
                          .updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                                 .setDisplayName(displayName)
                                                 .build())
                          .addOnSuccessListener(aVoid -> {
                              _registerResult.setValue(new RegisterResult(new User(authResult.getUser())));
                              DBUsersManager.addUser(User.getCurrentUser());
                          })
                          .addOnFailureListener(e -> _registerResult.setValue(new RegisterResult(e.getMessage())));
            }).addOnFailureListener(e -> _registerResult.setValue(new RegisterResult(e.getMessage())));
    }

    public void registerDataChanged(String displayName, String email, String password)
    {
        if (!isDisplayNameValid(displayName))
        {
            _registerFormState.setValue(new RegisterFormState(R.string.invalid_display_name, null, null));
        }
        else if (!isEmailValid(email))
        {
            _registerFormState.setValue(new RegisterFormState(null, R.string.invalid_email, null));
        }
        else if (!isPasswordValid(password))
        {
            _registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_password));
        }
        else
        {
            _registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isDisplayNameValid(String displayName)
    {
        return displayName != null && displayName.trim().length() >= 3;
    }

    private boolean isPasswordValid(String password)
    {
        return password != null && password.trim().length() >= 6;
    }

    private boolean isEmailValid(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
