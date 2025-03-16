package dev.android.project.ui.register;

import android.content.Intent;
import android.os.Bundle;
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
import dev.android.project.databinding.ActivityRegisterBinding;
import dev.android.project.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity
{
    private RegisterViewModel _registerViewModel;
    private ActivityRegisterBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        _registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        final EditText etDisplayName = _binding.etDisplayName;
        final EditText etEmail = _binding.etEmail;
        final EditText etPassword = _binding.etPassword;
        final Button btnRegister = _binding.btnRegister;
        final TextView tvLoginRedirect = _binding.tvLoginRedirect;
        final ProgressBar loadingProgressBar = _binding.loading;

        _registerViewModel.getRegisterFormState().observe(this, registerFormState ->
        {
            if (registerFormState == null)
                return;

            btnRegister.setEnabled(registerFormState.isDataValid());
            if (registerFormState.getDisplayNameError() != null)
                etDisplayName.setError(getString(registerFormState.getDisplayNameError()));
            if (registerFormState.getEmailError() != null)
                etEmail.setError(getString(registerFormState.getEmailError()));
            if (registerFormState.getPasswordError() != null)
                etPassword.setError(getString(registerFormState.getPasswordError()));
        });

        _registerViewModel.getRegisterResult().observe(this, registerResult ->
        {
            if (registerResult == null)
                return;

            loadingProgressBar.setVisibility(View.GONE);
            if (registerResult.getError() != null)
            {
                showRegisterFailed(registerResult.getError());
                return;
            }
            if (registerResult.getSuccess() != null)
            {
                updateUiWithUser(registerResult.getSuccess());
            }
            setResult(RESULT_OK);
            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s)
            {
                _registerViewModel.registerDataChanged(etDisplayName.getText().toString(),
                                                       etEmail.getText().toString(),
                                                       etPassword.getText().toString());
            }
        };


        etDisplayName.addTextChangedListener(afterTextChangedListener);
        etEmail.addTextChangedListener(afterTextChangedListener);
        etPassword.addTextChangedListener(afterTextChangedListener);
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                _registerViewModel.register(etDisplayName.getText().toString(),
                                            etEmail.getText().toString(),
                                            etPassword.getText().toString());
            }
            return false;
        });

        btnRegister.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            _registerViewModel.register(etDisplayName.getText().toString(),
                                        etEmail.getText().toString(),
                                        etPassword.getText().toString());
        });
        tvLoginRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateUiWithUser(User model)
    {
        String welcome = getString(R.string.register_successful) + model.getName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT).show();
    }

    private void showRegisterFailed(String errorString)
    {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}