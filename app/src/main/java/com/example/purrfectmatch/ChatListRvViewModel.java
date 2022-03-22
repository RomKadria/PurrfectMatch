package com.example.purrfectmatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.purrfectmatch.model.ChatMessage;
import com.example.purrfectmatch.model.ChatMessagesModel;
import com.example.purrfectmatch.model.ChatPet;
import com.example.purrfectmatch.model.ChatsModel;
import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

import java.util.List;

public class ChatListRvViewModel extends ViewModel {
    LiveData<List<ChatPet>> data;

    public ChatListRvViewModel(String petId){
        data = ChatsModel.instance.getAllChatPets(petId);
    }

    public LiveData<List<ChatPet>> getData() {
        return data;
    }
}