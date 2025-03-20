package dev.android.project.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import dev.android.project.R;
import dev.android.project.data.model.User;
import dev.android.project.data.providers.Firebase.FBAuth;

public class LoginViewModel extends ViewModel
{
    private final MutableLiveData<LoginFormState> _loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> _loginResult = new MutableLiveData<>();
    private final FirebaseAuth AUTH;

    LoginViewModel()
    {
        AUTH = FBAuth.getInstance();
    }

    LiveData<LoginFormState> getLoginFormState()
    {
        return _loginFormState;
    }

    LiveData<LoginResult> getLoginResult()
    {
        return _loginResult;
    }

    public void login(String email, String password)
    {

        AUTH.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> _loginResult.setValue(new LoginResult(new User(authResult.getUser()))))
            .addOnFailureListener(e -> _loginResult.setValue(new LoginResult(e.getMessage())));
    }

    public void loginDataChanged(String email, String password)
    {
        if (!isEmailValid(email))
        {
            _loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        }
        else if (!isPasswordValid(password))
        {
            _loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
        else
        {
            _loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isEmailValid(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password)
    {
        return password != null && password.trim().length() >= 6;
    }
}