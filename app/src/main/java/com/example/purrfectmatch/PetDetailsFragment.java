package com.example.purrfectmatch;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

public class PetDetailsFragment extends Fragment {
    TextView headerTv;
    TextView descriptionTv;
    TextView addressTv;
    ImageView petImg;
    TextView contactTv;
    ProgressBar progressBar;
    ImageButton mapImageBtn;
    String petId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_details, container, false);

        headerTv = view.findViewById(R.id.pet_details_header_tv);
        descriptionTv = view.findViewById(R.id.pet_details_description_tv);
        addressTv = view.findViewById(R.id.pet_details_address_tv);
        petImg = view.findViewById(R.id.pet_details_img);
        contactTv = view.findViewById(R.id.pet_details_contact_tv);
        progressBar = view.findViewById(R.id.pet_details_progressbar);
        mapImageBtn = view.findViewById(R.id.pet_details_map_btn);
        petId = PetDetailsFragmentArgs.fromBundle(getArguments()).getPetId();

        mapImageBtn.setOnClickListener(v -> navMap(v));

        Model.instance.getPetById(petId, new Model.GetPetById() {
            @Override
            public void onComplete(Pet pet) {
                if (pet.getPetUrl() != null) {
                    Picasso.get()
                            .load(pet.getPetUrl())
                            .error(R.drawable.pet_avatar)
                            .into(petImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            headerTv.setText(pet.getName() + ", " + pet.getAge());
                            descriptionTv.setText(pet.getDescription());
                            addressTv.setText(pet.getAddress());

                            progressBar.setVisibility(View.GONE);
                            petImg.setVisibility(View.VISIBLE);
                            headerTv.setVisibility(View.VISIBLE);
                            descriptionTv.setVisibility(View.VISIBLE);
                            addressTv.setVisibility(View.VISIBLE);
                            contactTv.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }
        });

        return view;
    }

    private void navMap(View v) {
        PetDetailsFragmentDirections.ActionPetDetailsFragmentToAllLocationsMapFragment action =
                PetDetailsFragmentDirections.actionPetDetailsFragmentToAllLocationsMapFragment(
                    petId
                );
        Navigation.findNavController(v).navigate(action);

//        Navigation.findNavController(v).navigate(PetDetailsFragmentDirections.actionPetDetailsFragmentToAllLocationsMapFragment());
    }
}