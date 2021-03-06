package com.example.purrfectmatch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.purrfectmatch.model.Model;

public class SignupSOneFragment extends Fragment {

    EditText etEmail, etPassword, etConfirmPassword;
    Button btnNext;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean mailValid = false;

    // menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.blank_menu, menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_s_one, container, false);

        setHasOptionsMenu(true);

        etEmail = view.findViewById(R.id.sso_email_et);
        etPassword = view.findViewById(R.id.sso_pass_et);
        etConfirmPassword = view.findViewById(R.id.sso_confirm_pass_et);
        btnNext = view.findViewById(R.id.sso_next_btn);

        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateEmail();
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validatePassword();
                if (!etConfirmPassword.getText().toString().isEmpty())
                    validateConfirmPassword();
            }
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateConfirmPassword();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (validateAll()) {
                SignupSOneFragmentDirections.ActionSignupSOneFragmentToSignupSTwoFragment action =
                        SignupSOneFragmentDirections.actionSignupSOneFragmentToSignupSTwoFragment(
                                etEmail.getText().toString().trim(),
                                etPassword.getText().toString()
                        );
                Navigation.findNavController(v).navigate(action);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    public void validateEmail() {
        String email = etEmail.getText().toString().trim();
        boolean matchFound = email.matches(emailPattern);

        Model.instance.checkEmailValid(email, exists -> {
            if (exists) {
                etEmail.setError("Email address already exists");
                mailValid = false;
            } else if (!matchFound) {
                etEmail.setError("Invalid email address");
                mailValid = false;
            } else {
                mailValid = true;
            }
        });
    }

    public boolean validatePassword() {
        if (etPassword.getText().toString().length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    public boolean validateConfirmPassword() {
        String password = etPassword.getText().toString();
        String cPassword = etConfirmPassword.getText().toString();
        if (password.matches(cPassword)) {
            etConfirmPassword.setError(null);
            return true;
        } else {
            etConfirmPassword.setError("Passwords don't match");
            return false;
        }
    }

    public boolean validateAll() {
        boolean flag = true;

        if (!validateConfirmPassword()) {
            etConfirmPassword.requestFocus();
            flag = false;
        }
        if (!validatePassword()) {
            etPassword.requestFocus();
            flag = false;
        }

        validateEmail();
        if (!mailValid) {
            etEmail.requestFocus();
            flag = false;
        }

        return flag;
    }
}