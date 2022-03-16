package com.example.purrfectmatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.purrfectmatch.model.ChatMessage;
import com.example.purrfectmatch.model.ChatMessagesModel;

import java.util.List;

public class ChatMessagesViewModel extends ViewModel {
    LiveData<List<ChatMessage>> data;

    public ChatMessagesViewModel(){
        data = ChatMessagesModel.instance.getAllChatMessages();
    }
    public LiveData<List<ChatMessage>> getData() {
        return data;
    }
}
