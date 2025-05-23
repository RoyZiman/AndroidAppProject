package dev.android.project.ui.fragments.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dev.android.project.R;
import dev.android.project.data.model.User;
import dev.android.project.databinding.ActivityLoginBinding;
import dev.android.project.ui.fragments.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity
{
    private LoginViewModel _loginViewModel;
    private ActivityLoginBinding _binding;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());


        _loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText etEmail = _binding.etEmail;
        final EditText etPassword = _binding.etPassword;
        final Button btnLogin = _binding.btnLogin;
        final TextView tvRegisterRedirect = _binding.tvRegisterRedirect;
        final ProgressBar loadingProgressBar = _binding.loading;


        _loginViewModel.getLoginFormState().observe(this, loginFormState ->
        {
            if (loginFormState == null)
                return;

            btnLogin.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getEmailError() != null)
                etEmail.setError(getString(loginFormState.getEmailError()));
            if (loginFormState.getPasswordError() != null)
                etPassword.setError(getString(loginFormState.getPasswordError()));
        });

        _loginViewModel.getLoginResult().observe(this, loginResult ->
        {
            if (loginResult == null)
                return;
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                _loginViewModel.loginDataChanged(etEmail.getText().toString(),
                                                 etPassword.getText().toString());
            }
        };
        etEmail.addTextChangedListener(afterTextChangedListener);
        etPassword.addTextChangedListener(afterTextChangedListener);
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                _loginViewModel.login(etEmail.getText().toString(),
                                      etPassword.getText().toString());
            }
            return false;
        });

        btnLogin.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            _loginViewModel.login(etEmail.getText().toString(),
                                  etPassword.getText().toString());
        });
        tvRegisterRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateUiWithUser(User model)
    {
        String welcome = getString(R.string.login_successful) + model.getName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString)
    {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}