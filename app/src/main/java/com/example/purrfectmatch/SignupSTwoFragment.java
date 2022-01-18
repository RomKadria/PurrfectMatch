package com.example.purrfectmatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Context;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.purrfectmatch.model.Model;
import com.squareup.picasso.Picasso;

import com.example.purrfectmatch.model.Pet;

import java.io.IOException;

public class SignupSTwoFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESULT_SUCCESS = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_IMAGE = 22;
    private final int PICK_IMAGE_REQUEST = 22;

    EditText nameEt;
    EditText ageEt;
    EditText addressEt;
    EditText aboutEt;
    Button signUpBtn;
    Button uploadBtn;
    Bitmap imageBitmap;
    ImageView iv;
    private Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_s_two, container, false);

        nameEt = view.findViewById(R.id.signupSTwo_name_et);
        ageEt = view.findViewById(R.id.signupSTwo_age_et);
        addressEt = view.findViewById(R.id.signupSTwo_address_et);
        aboutEt = view.findViewById(R.id.signupSTwo_about_et);
        signUpBtn = view.findViewById(R.id.signupSTwo_signup_btn);
        uploadBtn = view.findViewById(R.id.signupSTwo_upload_btn);
        iv = view.findViewById(R.id.imgView);

        signUpBtn.setOnClickListener(v -> signUp());
        uploadBtn.setOnClickListener(v -> upload());


        return view;
    }

    private void signUp() {
        if (nameEt.getText().toString().isEmpty() ||
            ageEt.getText().toString().isEmpty() ||
            addressEt.getText().toString().isEmpty() ||
            aboutEt.getText().toString().isEmpty()) {

            Context context = getContext();
            CharSequence text = "Please fill all fields";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();


        } else {
            Pet pet = new Pet();
        }
    }

        private void upload() {
            Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT, null);
            galleryintent.setType("image/*");

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INTENT, galleryintent);
            chooser.putExtra(Intent.EXTRA_TITLE, "how would you like to get your photo");

            Intent[] intentArray =  {cameraIntent};
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooser,SELECT_IMAGE);
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    Bitmap photo;
                    if (data.getData() != null) {
                        filePath = data.getData();
                         photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    } else {
                         photo = (Bitmap) data.getExtras().get("data");
                    }
                    iv.setImageBitmap(photo);
                    Model.instance.saveImage(photo, "rrr.jpg", url -> {
                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
    }
}