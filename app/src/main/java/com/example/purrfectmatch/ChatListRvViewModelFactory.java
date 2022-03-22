package com.example.purrfectmatch;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatListRvViewModelFactory implements ViewModelProvider.Factory{
    private String mPetId;

    public ChatListRvViewModelFactory(String petId) {
        mPetId = petId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ChatListRvViewModel(mPetId);
    }
}