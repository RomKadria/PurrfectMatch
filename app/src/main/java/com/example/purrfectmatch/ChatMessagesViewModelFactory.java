package com.example.purrfectmatch;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatMessagesViewModelFactory implements ViewModelProvider.Factory {
    private String mSendingId;
    private String mReceivingId;

    public ChatMessagesViewModelFactory(String sendingId, String receivingId) {
        mSendingId = sendingId;
        mReceivingId = receivingId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ChatMessagesViewModel(mSendingId, mReceivingId);
    }
}
