package com.example.purrfectmatch;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupSOneFragment extends Fragment {

    EditText etEmail, etPassword, etConfirmPassword;
    Button btnNext;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_s_one, container, false);

        etEmail = view.findViewById(R.id.sso_email_et);
        etPassword = view.findViewById(R.id.sso_pass_et);
        etConfirmPassword = view.findViewById(R.id.sso_confirm_pass_et);
        btnNext = view.findViewById(R.id.sso_next_btn);

        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (etEmail.getText().toString().trim().matches(emailPattern)) {
                    Toast.makeText(getContext().getApplicationContext(), "Valid Email Address", Toast.LENGTH_SHORT).show();
                } else {
                    etEmail.setError("Invalid Email Address");
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if(etEmail.getError() != null)
                etEmail.requestFocus();
            else if(etConfirmPassword.getError() != null)
                etConfirmPassword.requestFocus();
            //else
        });

        return view;
    }
}