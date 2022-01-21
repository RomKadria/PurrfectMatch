package com.example.purrfectmatch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
            if(validateAll()) {
                //Send parameters to next screen

                

                Navigation.findNavController(v).navigate(R.id.action_signupSOneFragment_to_signupSTwoFragment);
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

        if (!isExists[0]) {
            etEmail.setError("Email address already exists");
            return false;
        }
        else if (!matchFound) {
            etEmail.setError("Invalid email address");
            return false;
        }

        return true;
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
        if (password.matches(cPassword))
            return true;
        else {
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
        if(!validatePassword()) {
            etPassword.requestFocus();
            flag = false;
        }
        if(!validateEmail()) {
            etEmail.requestFocus();
            flag = false;
        }

        return flag;
    }
}