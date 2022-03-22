package com.example.purrfectmatch.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatsModel {
    public static final ChatsModel instance = new ChatsModel();
    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public enum LoadingState {
        loading,
        loaded
    }
    MutableLiveData<ChatsModel.LoadingState> chatPetsListLoadingState = new MutableLiveData<ChatsModel.LoadingState>();
    public LiveData<ChatsModel.LoadingState> getChatPetsListLoadingState() {
        return chatPetsListLoadingState;
    }

    ModelFirebase modelFirebase = new ModelFirebase();
    private ChatsModel(){
        chatPetsListLoadingState.setValue(ChatsModel.LoadingState.loaded);
    }

    MutableLiveData<List<ChatPet>> chatPetsList = new MutableLiveData<List<ChatPet>>();
    public LiveData<List<ChatPet>> getAllChatPets(String petId){
        refreshChatsList(petId);
        return chatPetsList;
    }
    public void refreshChatsList(String petId) {
        chatPetsListLoadingState.setValue(ChatsModel.LoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("chatPetsLastUpdateDate",0);

        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllChatPets(lastUpdateDate, petId, new ModelFirebase.GetAllChatPetsListener() {
            @Override
            public void onComplete(List<ChatPet> list) {
                // add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        for (ChatPet pet: list) {
                            AppLocalDb.db.chatPetDao().insertAll(pet);
                            if (lud < pet.getUpdateDate()){
                                lud = pet.getUpdateDate();
                            }
                        }

                        // update last local update date
                        MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit()
                        .putLong("chatPetsLastUpdateDate", lud)
                        .commit();

                        //return all data to caller
                        List<ChatPet> resList = AppLocalDb.db.chatPetDao().getAllConnectedPetChats(petId);
                        chatPetsList.postValue(resList);
                        chatPetsListLoadingState.postValue(ChatsModel.LoadingState.loaded);
                    }
                });
                chatPetsList.postValue(list);
                chatPetsListLoadingState.postValue(ChatsModel.LoadingState.loaded);
            }
        });
    }
}
