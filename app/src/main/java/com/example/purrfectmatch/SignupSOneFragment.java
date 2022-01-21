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

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

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
                validateEmail();
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!validatePassword())
                    etPassword.setError("Password must be at least 6 characters");
                else if (!validateConfirmPassword())
                    etConfirmPassword.setError("Password doesn't match");
            }
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!validateConfirmPassword())
                    etConfirmPassword.setError("Password doesn't match");
            }
        });

        btnNext.setOnClickListener(v -> {
            if(validateAll()) {
                //Send parameters to next screen
                Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public boolean validateEmail() {
        final boolean[] isExists = {true};
        String email = etEmail.getText().toString().trim();
        boolean matchFound = email.matches(emailPattern);

//        Model.instance.getPetById(email, new Model.GetPetById() {
//            @Override
//            public void onComplete(Pet pet) {
//                if (pet != null)
//                    isExists[0] = false;
//            }
//        });

        if (isExists[0]) {
            etEmail.setError("Email address already exists");
            return false;
        } else if (!matchFound) {
            etEmail.setError("Invalid email address");
            return false;
        }

        return true;
    }

    public boolean validatePassword() {
        return (etPassword.getText().toString().length() >= 6);
    }

    public boolean validateConfirmPassword() {
        String password = etPassword.getText().toString();
        String cPassword = etConfirmPassword.getText().toString();
        return password.matches(cPassword);
    }

    public boolean validateAll() {
        if(etEmail.getError() != null) {
            etEmail.requestFocus();
            return false;
        }
        else if(etPassword.getError() != null) {
            etPassword.requestFocus();
            return false;
        }
        else if (etConfirmPassword.getError() != null) {
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}