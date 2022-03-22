package com.example.purrfectmatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

import java.util.List;

public class PetListRvViewModel extends ViewModel {
    LiveData<List<Pet>> data;

    public PetListRvViewModel(){
        data = Model.instance.getAllPets();
    }
    public LiveData<List<Pet>> getData() {
        return data;
    }
}
