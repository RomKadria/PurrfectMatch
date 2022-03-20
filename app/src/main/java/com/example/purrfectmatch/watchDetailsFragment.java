package com.example.purrfectmatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.purrfectmatch.model.Model;

import com.example.purrfectmatch.model.Pet;

import java.io.IOException;

public class watchDetailsFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESULT_SUCCESS = 0;
    final static int MAX_AGE = 200;
    final static int MIN_AGE = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_IMAGE = 22;
    private final int PICK_IMAGE_REQUEST = 22;

    EditText nameEt;
    EditText ageEt;
    EditText addressEt;
    EditText aboutEt;
    Button updateBtn;
    Button uploadBtn;
    Button editBtn;
    ImageButton mapBtn;
    Bitmap imageBitmap;
    ImageView imageIv;
    private Uri filePath;
    Bitmap photo;
    ProgressBar progressBar;
    Double latitude;
    Double longitude;
    String email;
    String password;
    Boolean isEdit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_details, container, false);

//        if (getArguments() != null) {
////            email = watchDetailsFragmentArgs.fromBundle(getArguments()).getEmail();
////            password = watchDetailsFragmentArgs.fromBundle(getArguments()).getPassword();
////        }

        nameEt = view.findViewById(R.id.watchDetails_name_et);
        ageEt = view.findViewById(R.id.watchDetails_age_et);
        addressEt = view.findViewById(R.id.watchDetails_address_et);
        aboutEt = view.findViewById(R.id.watchDetails_about_et);
        updateBtn = view.findViewById(R.id.watchDetails_update_btn);
        uploadBtn = view.findViewById(R.id.watchDetails_upload_btn);
        editBtn = view.findViewById(R.id.watchDetails_edit_btn);
        mapBtn = view.findViewById(R.id.watchDetails_map_btn);
        imageIv = view.findViewById(R.id.watchDetails_image_iv);
        progressBar = view.findViewById(R.id.watchDetails_progressbar);
        progressBar.setVisibility(View.GONE);
        mapBtn.setEnabled(false);
        updateBtn.setOnClickListener(v -> update(v));
        uploadBtn.setOnClickListener(v -> upload());
        editBtn.setOnClickListener(v -> toggleEdit());
        mapBtn.setOnClickListener(v -> openMap(v));
        ageEt.setHint("Between " + MIN_AGE + " and " + MAX_AGE);
        nameEt.setEnabled(false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = NavHostFragment.findNavController(this);

        MutableLiveData<Address> liveData = navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData("address");
        liveData.observe(getViewLifecycleOwner(), address -> {
            toggleEdit();
            addressEt.setText(address.getAddressLine(0));
            latitude = address.getLatitude();
            longitude = address.getLongitude();
        });
    }

    private void update(View v) {

        progressBar.setVisibility(View.VISIBLE);

        if (nameEt.getText().toString().isEmpty() ||
                ageEt.getText().toString().isEmpty() ||
                addressEt.getText().toString().isEmpty() ||
                aboutEt.getText().toString().isEmpty() ||
                photo == null) {

            Context context = getContext();
            CharSequence text = "Please fill all fields";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            progressBar.setVisibility(View.GONE);



        } else {
            String name = nameEt.getText().toString();
            int age = Integer.parseInt(ageEt.getText().toString());
            String address = addressEt.getText().toString();
            String about = aboutEt.getText().toString();

            if (age < MIN_AGE || age > MAX_AGE) {
                Toast.makeText(getActivity(), "Age must be between " + MIN_AGE + " and " + MAX_AGE, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
//            } else if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(getActivity(), "missing data from last phase, please restart the process", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
            } else { // All good lets add the pet
                Model.instance.saveImage(photo, email + ".jpg", url -> {

                    Pet pet = new Pet("a@a.a", name, age, address, about, "123456", url, latitude, longitude);
                    Model.instance.updatePet(pet, () -> {
                        progressBar.setVisibility(View.GONE);

                        toggleEdit();
                    });
                });
            }
        }
    }

    private  void toggleEdit() {
        if (!isEdit) {
            nameEt.setEnabled(true);
            ageEt.setEnabled(true);
            aboutEt.setEnabled(true);
            updateBtn.setVisibility(View.VISIBLE);
            mapBtn.setEnabled(true);
            isEdit = true;
        } else {
            nameEt.setEnabled(false);
            ageEt.setEnabled(false);
            aboutEt.setEnabled(false);
            updateBtn.setVisibility(View.INVISIBLE);
            mapBtn.setEnabled(false);
            isEdit = false;
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

    private void openMap(View v) {
        toggleEdit();
        Navigation.findNavController(v).navigate(watchDetailsFragmentDirections.actionWatchDetailsFragmentToUserLocationMapFragment());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    if (data.getData() != null) {
                        filePath = data.getData();
                        photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    } else {
                        photo = (Bitmap) data.getExtras().get("data");
                    }

                    imageIv.setImageBitmap(photo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }
}