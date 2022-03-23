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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;
import com.example.purrfectmatch.model.SaveSharedPreference;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.io.IOException;

public class watchDetailsFragment extends Fragment {
    final static int MAX_AGE = 200;
    final static int MIN_AGE = 0;
    private static final int SELECT_IMAGE = 22;

    EditText nameEt;
    EditText ageEt;
    EditText addressEt;
    EditText aboutEt;
    Button updateBtn;
    Button uploadBtn;
    Button editBtn;
    ImageButton mapBtn;
    ImageView petImageIv;
    TextView picTv;
    private Uri filePath;
    Bitmap photo;
    ProgressBar progressBar;
    Double latitude;
    Double longitude;
    String password;
    Boolean isEdit = false;
    String petId;
    String currentUrl;
    boolean isFirstTime = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_details, container, false);
        setHasOptionsMenu(true);

        petId = SaveSharedPreference.getEmail(this.getActivity().getApplicationContext());
        password = SaveSharedPreference.getPassword(this.getActivity().getApplicationContext());
        nameEt = view.findViewById(R.id.watchDetails_name_et);
        ageEt = view.findViewById(R.id.watchDetails_age_et);
        addressEt = view.findViewById(R.id.watchDetails_address_et);
        aboutEt = view.findViewById(R.id.watchDetails_about_et);
        updateBtn = view.findViewById(R.id.watchDetails_update_btn);
        uploadBtn = view.findViewById(R.id.watchDetails_upload_btn);
        editBtn = view.findViewById(R.id.watchDetails_edit_btn);
        mapBtn = view.findViewById(R.id.watchDetails_map_btn);
        petImageIv = view.findViewById(R.id.watchDetails_petimage_iv);
        picTv = view.findViewById(R.id.watchDetails_pic_tv);
        progressBar = view.findViewById(R.id.watchDetails_progressbar);
        progressBar.setVisibility(View.GONE);
        mapBtn.setEnabled(false);
        updateBtn.setOnClickListener(v -> update(v));
        uploadBtn.setOnClickListener(v -> upload());
        editBtn.setOnClickListener(v -> toggleEdit());
        mapBtn.setOnClickListener(v -> openMap(v));
        ageEt.setHint("Between " + MIN_AGE + " and " + MAX_AGE);


        if (isFirstTime) {
            Model.instance.getPetById(petId, pet -> {
                if (pet.getPetUrl() != null) {
                    Picasso.get()
                            .load(pet.getPetUrl())
                            .error(R.drawable.pet_avatar)
                            .into(petImageIv, new Callback() {
                                @Override
                                public void onSuccess() {
                                    nameEt.setText(pet.getName());
                                    ageEt.setText("" + pet.getAge());
                                    aboutEt.setText(pet.getDescription());
                                    addressEt.setText(pet.getAddress());
                                    currentUrl = pet.getPetUrl();
                                    progressBar.setVisibility(View.GONE);
                                    latitude = pet.getLatitude();
                                    longitude = pet.getLongitude();
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                }
            });

            isFirstTime = false;
        }

        return view;
    }

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
                aboutEt.getText().toString().isEmpty()) {

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
            } else if (petId.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "missing data, please reload app and try again", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else { // All good lets add the pet
                if (photo != null) {
                    Model.instance.saveImage(photo, petId + ".jpg", url -> {

                        Pet pet = new Pet(petId, name, age, address, about, password, url, latitude, longitude);
                        Model.instance.updatePet(pet, () -> {
                            progressBar.setVisibility(View.GONE);

                        });
                    });
                } else {
                    Pet pet = new Pet(petId, name, age, address, about, password, currentUrl, latitude, longitude);
                    Model.instance.updatePet(pet, () -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "User updated", Toast.LENGTH_SHORT).show();
                    });
                }

                toggleEdit();
            }
        }
    }

    private void toggleEdit() {
        if (!isEdit) {
            nameEt.setEnabled(true);
            ageEt.setEnabled(true);
            aboutEt.setEnabled(true);
            editBtn.setText("Cancel");
            updateBtn.setVisibility(View.VISIBLE);
            uploadBtn.setVisibility(View.VISIBLE);
            picTv.setVisibility(View.VISIBLE);
            mapBtn.setEnabled(true);
            isEdit = true;
        } else {
            nameEt.setEnabled(false);
            ageEt.setEnabled(false);
            aboutEt.setEnabled(false);
            editBtn.setText("Edit");
            updateBtn.setVisibility(View.INVISIBLE);
            uploadBtn.setVisibility(View.INVISIBLE);
            picTv.setVisibility(View.INVISIBLE);
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

                    petImageIv.setImageBitmap(photo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }
}