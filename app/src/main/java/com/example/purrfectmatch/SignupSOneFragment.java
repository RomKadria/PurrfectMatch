package com.example.purrfectmatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.purrfectmatch.model.Model;
import com.squareup.picasso.Picasso;

import com.example.purrfectmatch.model.Pet;

import java.io.IOException;


public class SignupSOneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_s_one, container, false);

        Button btn = view.findViewById(R.id.button);

        btn.setOnClickListener(v -> signUp(view));


        return view;
    }

    private void signUp(View view) {
        Toast.makeText(getActivity(), "Age must be between", Toast.LENGTH_SHORT).show();


        Navigation.findNavController(view).navigate(R.id.action_signupSOneFragment_to_signupSTwoFragment);
    }
}