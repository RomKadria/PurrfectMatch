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

    public ChatListRvViewModel(){ }

    public LiveData<List<ChatPet>> getData(String petId) {
        if (data == null) {
            data = ChatsModel.instance.getAllChatPets(petId);
        }
        return data;
    }
}