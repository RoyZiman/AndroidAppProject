package dev.android.project.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;

import dev.android.project.data.model.User;
import dev.android.project.R;
import dev.android.project.data.providers.FBAuth;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private FirebaseAuth AUTH;

    LoginViewModel() {
        AUTH = FBAuth.getInstance();
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password)
    {
        AUTH.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                loginResult.setValue(new LoginResult(new User(authResult.getUser())));
            }).addOnFailureListener(e -> {
                loginResult.setValue(new LoginResult(e.getMessage()));
            });
    }

    public void loginDataChanged(String email, String password) {
        if (!iEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean iEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 6;
    }
}