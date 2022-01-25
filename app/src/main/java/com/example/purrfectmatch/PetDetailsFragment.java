package com.example.purrfectmatch;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

public class PetDetailsFragment extends Fragment {
    TextView headerTv;
    TextView descriptionTv;
    TextView addressTv;
    ImageView petImg;
    TextView contactTv;
    ProgressBar progressBar;

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
        String petId = "asdasd@gmail.com";
//        String petId = PetDetailsFragmentArgs.fromBundle(getArguments()).getPetId();

        Model.instance.getPetById(petId, new Model.GetPetById() {
            @Override
            public void onComplete(Pet pet) {
                headerTv.setText(pet.getName() + ", " + pet.getAge());
                descriptionTv.setText(pet.getDescription());
                addressTv.setText(pet.getAddress());
                if (pet.getPetUrl() != null) {
                    Picasso.get().load(pet.getPetUrl()).into(petImg);
                }
                progressBar.setVisibility(View.GONE);
                petImg.setVisibility(View.VISIBLE);
                headerTv.setVisibility(View.VISIBLE);
                descriptionTv.setVisibility(View.VISIBLE);
                addressTv.setVisibility(View.VISIBLE);
                contactTv.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}