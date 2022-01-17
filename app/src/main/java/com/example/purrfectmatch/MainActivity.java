package com.example.purrfectmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signInButton = (Button) findViewById(R.id.signin_signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText emailInput = (EditText) findViewById(R.id.signin_email_input);
                EditText passwordInput = (EditText) findViewById(R.id.signin_password_input);

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();


            }
        });

        Pet pet = new Pet("blazing","123", "rexi@gmail.com", "12345");
        Model.instance.addPet(pet);

    }
}