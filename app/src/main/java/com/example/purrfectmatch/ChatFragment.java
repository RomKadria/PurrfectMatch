package com.example.purrfectmatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.purrfectmatch.model.ChatMessage;
import com.example.purrfectmatch.model.ChatMessagesModel;
import com.squareup.picasso.Picasso;

public class ChatFragment extends Fragment {
    ChatMessagesViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    String sendingPetId;
    String receivingPetId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ChatMessagesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

//        sendingPetId = ChatFragmentArgs.fromBundle(getArguments()).getSendingPetId();
//        receivingPetId = ChatFragmentArgs.fromBundle(getArguments()).getReceivingPetId();
// TODO: remove this 
        sendingPetId = "a@a.a";
        receivingPetId = "a@a.b";


        swipeRefresh = view.findViewById(R.id.chat_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> ChatMessagesModel.instance.refreshChatMessages(sendingPetId, receivingPetId));

        RecyclerView list = view.findViewById(R.id.list_of_messages);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        setHasOptionsMenu(true);
        viewModel.getData(sendingPetId, receivingPetId).observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(ChatMessagesModel.instance.getChatMessagesLoadingState().getValue() == ChatMessagesModel.LoadingState.loading);
        ChatMessagesModel.instance.getChatMessagesLoadingState().observe(getViewLifecycleOwner(), chatMessagesLoadingState -> {
            if (chatMessagesLoadingState == ChatMessagesModel.LoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }

        });
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView messageUser;
        public TextView messageTime;
        public TextView messageText;
        public ImageView editBtn;
        public ImageView deleteBtn;
        public EditText editMessageText;
        public Button editSaveBtn;
        public Button editCancelBtn;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            messageUser = itemView.findViewById(R.id.chat_message_user_tv);
            messageTime = itemView.findViewById(R.id.chat_message_time_tv);
            messageText = itemView.findViewById(R.id.chat_message_text_tv);
            editBtn = itemView.findViewById(R.id.chat_message_edit_btn);
            deleteBtn = itemView.findViewById(R.id.chat_message_delete_btn);
            editMessageText = itemView.findViewById(R.id.chat_message_text_et);
            editSaveBtn = itemView.findViewById(R.id.chat_message_edit_save_btn);
            editCancelBtn = itemView.findViewById(R.id.chat_message_edit_cancel_btn);

            editBtn.setOnClickListener(v -> {
                editMessageText.setText(messageText.getText());
                editMessageText.setVisibility(View.VISIBLE);
                editSaveBtn.setVisibility(View.VISIBLE);
                editCancelBtn.setVisibility(View.VISIBLE);
                messageText.setVisibility(View.GONE);
            });

            deleteBtn.setOnClickListener(v -> {
            });

            editSaveBtn.setOnClickListener(v -> {

            });

            editCancelBtn.setOnClickListener(v -> {
                editMessageText.setVisibility(View.GONE);
                editSaveBtn.setVisibility(View.GONE);
                editCancelBtn.setVisibility(View.GONE);
                messageText.setVisibility(View.VISIBLE);
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.fragment_chat_message, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ChatMessage chatMessage = viewModel.getData(sendingPetId, receivingPetId).getValue().get(position);

            holder.messageText.setText(chatMessage.getTextMessage());
            holder.messageUser.setText(chatMessage.getSendingId()); // TODO: get the name?
            holder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getMessageTime()));
//            Picasso.get().load(chatMessage.getPetUrl()).into(holder.petImage);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData(sendingPetId, receivingPetId).getValue() == null) {
                return 0;
            }
            return viewModel.getData(sendingPetId, receivingPetId).getValue().size();
        }
    }
}