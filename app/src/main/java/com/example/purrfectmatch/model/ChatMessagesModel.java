package com.example.purrfectmatch.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatMessagesModel {
    public static final ChatMessagesModel instance = new ChatMessagesModel();
    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public enum LoadingState {
        loading,
        loaded
    }

    MutableLiveData<LoadingState> chatMessagesLoadingState = new MutableLiveData<LoadingState>();

    public LiveData<LoadingState> getChatMessagesLoadingState() {
        return chatMessagesLoadingState;
    }

    ModelFirebase modelFirebase = new ModelFirebase();

    private ChatMessagesModel() {
        chatMessagesLoadingState.setValue(LoadingState.loaded);
    }

    MutableLiveData<List<ChatMessage>> chatMessages = new MutableLiveData<List<ChatMessage>>();

    public LiveData<List<ChatMessage>> getAllChatMessages(String sendingPetId, String receivingPetId) {
        chatMessagesLoadingState.setValue(LoadingState.loading);
        refreshChatMessages(sendingPetId, receivingPetId);
        return chatMessages;
    }

    public void refreshChatMessages(String sendingPetId, String receivingPetId) {
        chatMessagesLoadingState.setValue(LoadingState.loading);

        // get last local update date TODO: mark the specific chat
        Long lastUpdateDate = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("ChatMessagesLastUpdateDate" + receivingPetId, 0);

        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllChatMessages(lastUpdateDate, sendingPetId, receivingPetId, new ModelFirebase.GetAllChatsListener() {
            @Override
            public void onComplete(List<ChatMessage> list) {
                // add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        Log.d("TAG", "fb returned " + list.size());
                        for (ChatMessage chatMessage : list) {
                            AppLocalDb.db.chatMessageDao().insertAll(chatMessage);
                            if (lud < chatMessage.getMessageTime()) {
                                lud = chatMessage.getMessageTime();
                            }
                        }
                        // update last local update date
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("ChatMessagesLastUpdateDate" + receivingPetId, lud) // TODO: save for specific chat
                                .commit();

                        //return all data to caller
                        List<ChatMessage> allChatMessages = AppLocalDb.db.chatMessageDao().getAllChatMessages(sendingPetId, receivingPetId);
                        chatMessages.postValue(allChatMessages);
                        chatMessagesLoadingState.postValue(LoadingState.loaded);
                    }
                });
            }
        });
    }

    public interface AddChatMessageListener {
        void onComplete();
    }

    public interface UpdateChatMessageListener {
        void onComplete();
    }
    public interface DeleteChatMessageListener {
        void onComplete();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addChatMessage(ChatMessage chatMessage, AddChatMessageListener listener) {
        modelFirebase.addChatMessage(chatMessage, () -> {
            listener.onComplete();
            refreshChatMessages(chatMessage.sendingId, chatMessage.receivingId);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addChatMessage(ChatMessage chatMessage) {
        modelFirebase.addChatMessage(chatMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateChatMessage(ChatMessage chatMessage, UpdateChatMessageListener listener) {
        modelFirebase.updateChatMessage(chatMessage, () -> {
            listener.onComplete();
            refreshChatMessages(chatMessage.sendingId, chatMessage.receivingId);
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteChatMessage(ChatMessage chatMessage, DeleteChatMessageListener listener) {
        modelFirebase.updateChatMessage(chatMessage, () -> {
            listener.onComplete();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AppLocalDb.db.chatMessageDao().delete(chatMessage);

                    if (AppLocalDb.db.chatMessageDao().getAllChatMessages(chatMessage.sendingId, chatMessage.receivingId).isEmpty()) {
                        List<ChatPet> pets = AppLocalDb.db.chatPetDao().getById(chatMessage.receivingId);
                        if (!pets.isEmpty()) {
                            ChatPet petToDelete = pets.get(0);
                            AppLocalDb.db.chatPetDao().delete(petToDelete);
                        }
                    }
                }});
            refreshChatMessages(chatMessage.sendingId, chatMessage.receivingId);
        });
    }
}

