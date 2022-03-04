package com.example.purrfectmatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;


public class LoginFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login, container, false);

            Button signInButton = view.findViewById(R.id.signin_signin_button);
            Button signUpButton = view.findViewById(R.id.signin_signup_button);

            EditText emailInput = view.findViewById(R.id.signin_email_input);
            EditText passwordInput = view.findViewById(R.id.signin_password_input);
            CheckBox saveLoginCheckBox = view.findViewById(R.id.signin_login_checkbox);

            // if login is remembered
            SharedPreferences loginPreferences = this.getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
            Boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);
            if (saveLogin) {
                emailInput.setText(loginPreferences.getString("email", ""));
                passwordInput.setText(loginPreferences.getString("password", ""));
                saveLoginCheckBox.setChecked(true);
            }

            // on sign in
            signInButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast blankInputToast = Toast.makeText(getActivity().getApplicationContext(),
                            "You must fill all required fields!",
                            Toast.LENGTH_SHORT);

                    Toast wrongInputToast = Toast.makeText(getActivity().getApplicationContext(),
                            "Your email or password is incorrect",
                            Toast.LENGTH_SHORT);

                    Toast successInputToast = Toast.makeText(getActivity().getApplicationContext(),
                            "success",
                            Toast.LENGTH_SHORT);


                    String email = emailInput.getText().toString();
                    String password = passwordInput.getText().toString();


                    if (email.isEmpty() || password.isEmpty()) {
                        blankInputToast.show();

                    } else {
                        // TODO: check password & email in db
                        Pet pet = Model.instance.getPetByEmail(email, url -> {});

                        if ((pet == null) || (!pet.getEmail().equals(email)) || !(pet.getPassword().equals(password))){
                            wrongInputToast.show();
                        } else {

                            if (saveLoginCheckBox.isChecked()) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("username", email);
                                loginPrefsEditor.putString("password", password);
                                loginPrefsEditor.commit();
                            } else {
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }

                            successInputToast.show();

                            Navigation.findNavController(v)
                                      .navigate(LoginFragmentDirections
                                      .actionLoginFragmentToPetListRvFragment());                       }
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
    }