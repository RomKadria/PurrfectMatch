package com.example.purrfectmatch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.SaveSharedPreference;


public class LoginFragment extends Fragment {
    ProgressBar progressBar;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login, container, false);

            Button signInButton = view.findViewById(R.id.signin_signin_button);
            Button signUpButton = view.findViewById(R.id.signin_signup_button);

            progressBar = view.findViewById(R.id.signin_progressbar);
            progressBar.setVisibility(View.GONE);

            EditText emailInput = view.findViewById(R.id.signin_email_input);
            EditText passwordInput = view.findViewById(R.id.signin_password_input);
            CheckBox saveLoginCheckBox = view.findViewById(R.id.signin_login_checkbox);

            // fill fields
            emailInput.setText(SaveSharedPreference.getEmail(getActivity().getApplicationContext()));
            passwordInput.setText(SaveSharedPreference.getPassword(getActivity().getApplicationContext()));
            saveLoginCheckBox.setChecked(SaveSharedPreference.getLogin(getActivity().getApplicationContext()));

            // on sign in
            signInButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);

                    String email = emailInput.getText().toString();
                    String password = passwordInput.getText().toString();
                    Toast alertToast = null;


                    if (email.isEmpty() || password.isEmpty()) {
                        alertToast.makeText(getActivity().getApplicationContext(),
                                "You must fill all required fields!",
                                Toast.LENGTH_SHORT)
                                .show();

                        progressBar.setVisibility(View.GONE);


                    } else {
                        // user validation
                        Model.instance.checkUserValid(email, password, valid -> {

                            if (!valid){
                                alertToast.makeText(getActivity().getApplicationContext(),
                                        "Your email or password is incorrect",
                                        Toast.LENGTH_SHORT)
                                        .show();

                                progressBar.setVisibility(View.GONE);
                            } else {
                                setLoginPref(email, password, saveLoginCheckBox.isChecked());

                                alertToast.makeText(getActivity().getApplicationContext(),
                                        "success",
                                        Toast.LENGTH_SHORT)
                                        .show();

                                Navigation.findNavController(v)
                                        .navigate(LoginFragmentDirections
                                                .actionLoginFragmentToPetListRvFragment());
                            }
                        });
                    }
                }
            });

            // on sign up
            signUpButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Navigation.findNavController(v)
                              .navigate(LoginFragmentDirections
                              .actionLoginFragmentToSignupSOneFragment());
                }
            });

            return view;
        }

    @Override
        public void onResume() {
            super.onResume();
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        }

    @Override
        public void onStop() {
            super.onStop();
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        }

        public void setLoginPref(String email, String password, boolean isChecked) {
            SaveSharedPreference.setEmail(getActivity().getApplicationContext(), email);
            SaveSharedPreference.setPassword(getActivity().getApplicationContext(), password);

            if (isChecked) {
                SaveSharedPreference.setLogin(getActivity().getApplicationContext(), true);
            } else {
                SaveSharedPreference.setLogin(getActivity().getApplicationContext(), false);
            }
        }
    }