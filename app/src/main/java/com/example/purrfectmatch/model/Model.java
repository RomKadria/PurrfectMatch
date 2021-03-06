package com.example.purrfectmatch.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    Executor executor = Executors.newFixedThreadPool(1);

    public enum LoadingState {
        loading,
        loaded
    }

    MutableLiveData<LoadingState> petListLoadingState = new MutableLiveData<LoadingState>();

    public LiveData<LoadingState> getPetListLoadingState() {
        return petListLoadingState;
    }

    ModelFirebase modelFirebase = new ModelFirebase();

    private Model() {
        petListLoadingState.setValue(LoadingState.loaded);
    }

    MutableLiveData<List<Pet>> petsList = new MutableLiveData<List<Pet>>();

    public LiveData<List<Pet>> getAllPets() {
        if (petsList.getValue() == null) {
            refreshPetList();
        }
        ;
        return petsList;
    }

    public void refreshPetList() {
        petListLoadingState.setValue(LoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PetsLastUpdateDate", 0);

        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllPets(lastUpdateDate, new ModelFirebase.GetAllPetsListener() {
            @Override
            public void onComplete(List<Pet> list) {
                // add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        Log.d("TAG", "fb returned " + list.size());
                        for (Pet pet : list) {
                            AppLocalDb.db.petDao().insertAll(pet);
                            if (lud < pet.getUpdateDate()) {
                                lud = pet.getUpdateDate();
                            }
                        }
                        // update last local update date
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("PetsLastUpdateDate", lud)
                                .commit();

                        //return all data to caller
                        List<Pet> petList = AppLocalDb.db.petDao().getAll();
                        petsList.postValue(petList);
                        petListLoadingState.postValue(LoadingState.loaded);
                    }
                });
            }
        });
    }

    public interface AddPetListener {
        void onComplete();
    }

    public void addPet(Pet pet, AddPetListener listener) {
        modelFirebase.addPet(pet, listener);
    }

    public interface UpdatePetListener {
        void onComplete();
    }

    public void updatePet(Pet pet, UpdatePetListener listener) {
        modelFirebase.updatePet(pet, listener);
    }

    public interface GetPetById {
        void onComplete(Pet pet);
    }

    public Pet getPetById(String petId, GetPetById listener) {
        modelFirebase.getPetById(petId, listener);
        return null;
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }

    public interface OnUserCheckListener {
        void onComplete(boolean valid);
    }

    public void checkUserValid(String email, String password, OnUserCheckListener listener) {
        modelFirebase.checkUser(email, password, listener);
    }

    public interface OnEmailCheckListener {
        void onComplete(boolean exists);
    }

    public void checkEmailValid(String email, OnEmailCheckListener listener) {
        modelFirebase.checkEmail(email, listener);
    }
}

