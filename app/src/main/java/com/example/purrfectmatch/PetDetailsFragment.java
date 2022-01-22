package com.example.purrfectmatch;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

public class PetDetailsFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //viewModel = new ViewModelProvider(this).get(StudentListRvViewModel.class);
    }

    TextView headerTv;
    TextView descriptionTv;
    TextView addressTv;
    TextView phoneNumberTv;
    ImageView petImg;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_details, container, false);

        headerTv = view.findViewById(R.id.pet_details_header_tv);
        descriptionTv = view.findViewById(R.id.pet_details_description_tv);
        addressTv = view.findViewById(R.id.pet_details_address_tv);
        phoneNumberTv = view.findViewById(R.id.pet_details_PhoneNumber_tv);
        petImg = view.findViewById(R.id.pet_details_imv);
        progressBar = view.findViewById(R.id.pet_details_progressbar);
        String petId = "456";
//        String petId = PetDetailsFragmentArgs.fromBundle(getArguments()).getPetId();

        Model.instance.getPetById(petId, new Model.GetPetById() {
            @Override
            public void onComplete(Pet pet) {
                headerTv.setText(pet.getName() + ", " + pet.getAge());
                descriptionTv.setText(pet.getDescription());
                addressTv.setText(pet.getAddress());
                phoneNumberTv.setText(pet.getPhoneNumber());
//                if (student.getAvatarUrl() != null) {
//                    Picasso.get().load(student.getAvatarUrl()).into(avatarImv);
//                }
                progressBar.setVisibility(View.GONE);
                petImg.setVisibility(View.VISIBLE);
                headerTv.setVisibility(View.VISIBLE);
                descriptionTv.setVisibility(View.VISIBLE);
                addressTv.setVisibility(View.VISIBLE);
                phoneNumberTv.setVisibility(View.VISIBLE);
            }
        });

//        avatarImv = view.findViewById(R.id.details_avatar_img);

//        Button backBtn = view.findViewById(R.id.details_back_btn);
//        backBtn.setOnClickListener((v)->{
//            Navigation.findNavController(v).navigateUp();
//        });
        return view;
    }
}