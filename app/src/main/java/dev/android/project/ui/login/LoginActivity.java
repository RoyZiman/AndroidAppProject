package  dev.android.project.ui.login;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import dev.android.project.R;
import dev.android.project.data.model.User;
import dev.android.project.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity
{

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding _binding;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());


        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText etEmail = _binding.etEmail;
        final EditText etPassword = _binding.etPassword;
        final Button btnLogin = _binding.btnLogin;
        final ProgressBar loadingProgressBar = _binding.loading;

        loginViewModel.getLoginFormState().observe(this, loginFormState ->
        {
            if (loginFormState == null)
                return;

            btnLogin.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getEmailError() != null)
                etEmail.setError(getString(loginFormState.getEmailError()));
            if (loginFormState.getPasswordError() != null)
                etPassword.setError(getString(loginFormState.getPasswordError()));
        });

        loginViewModel.getLoginResult().observe(this, loginResult ->
        {
            if (loginResult == null)
            {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null)
            {
                showLoginFailed(loginResult.getError());
                return;
            }
            if (loginResult.getSuccess() != null)
            {
                updateUiWithUser(loginResult.getSuccess());
            }

            setResult(Activity.RESULT_OK);
            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                loginViewModel.loginDataChanged(etEmail.getText().toString(),
                                                etPassword.getText().toString());
            }
        };
        etEmail.addTextChangedListener(afterTextChangedListener);
        etPassword.addTextChangedListener(afterTextChangedListener);
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                loginViewModel.login(etEmail.getText().toString(),
                                     etPassword.getText().toString());
            }
            return false;
        });

        btnLogin.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(etEmail.getText().toString(),
                                 etPassword.getText().toString());
        });
    }

    private void updateUiWithUser(User model)
    {
        String welcome = getString(R.string.Login_successful) + model.getName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString)
    {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}