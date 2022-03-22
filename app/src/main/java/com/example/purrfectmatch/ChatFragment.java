package com.example.purrfectmatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purrfectmatch.model.ChatMessage;
import com.example.purrfectmatch.model.ChatMessagesModel;
import com.example.purrfectmatch.model.Model;
import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

public class ChatFragment extends Fragment {
    private static final int SELECT_IMAGE = 22;

    ChatMessagesViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    String sendingPetId;
    String receivingPetId;

    EditText chatEditText;
    Button chatSendBtn;
    ImageButton chatCamBtn;
    ImageView photoIndicatiomImg;
    private Uri filePath;
    Bitmap photo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        sendingPetId = ChatFragmentArgs.fromBundle(getArguments()).getSendingPetId();
        receivingPetId = ChatFragmentArgs.fromBundle(getArguments()).getReceivingPetId();

        viewModel = new ViewModelProvider(this, new ChatMessagesViewModelFactory(sendingPetId, receivingPetId)).get(ChatMessagesViewModel.class);

        swipeRefresh = view.findViewById(R.id.chat_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> ChatMessagesModel.instance.refreshChatMessages(sendingPetId, receivingPetId, true));

        // Refresh chat every 30 sec
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        ChatMessagesModel.instance.refreshChatMessages(sendingPetId, receivingPetId, false);
                    }
                },
                30000);

        RecyclerView list = view.findViewById(R.id.list_of_messages);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(ChatMessagesModel.instance.getChatMessagesLoadingState().getValue() == ChatMessagesModel.LoadingState.loading);
        ChatMessagesModel.instance.getChatMessagesLoadingState().observe(getViewLifecycleOwner(), chatMessagesLoadingState -> {
            if (chatMessagesLoadingState == ChatMessagesModel.LoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }

        });

        chatEditText = view.findViewById(R.id.chat_text_et);
        chatSendBtn = view.findViewById(R.id.chat_send_btn);
        chatCamBtn = view.findViewById(R.id.chat_cam_btn);
        photoIndicatiomImg = view.findViewById(R.id.chat_camera_img);

        chatCamBtn.setOnClickListener(v -> upload());
        chatSendBtn.setOnClickListener(v -> sendMsg());

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMsg() {
        String text = chatEditText.getText().toString();

        // Sending msg only if text was written / photo was taken
        if (!text.isEmpty() || photo != null) {

            String textMsg = chatEditText.getText().toString();
            ChatMessage msg = new ChatMessage(sendingPetId, receivingPetId, textMsg, null, null);
            if (photo == null){
                ChatMessagesModel.instance.addChatMessage(msg, () -> {});
            }else{
                Model.instance.saveImage(photo,Timestamp.now().toString() + ".jpg", url -> {
                    msg.setImgUrl(url);
                    ChatMessagesModel.instance.addChatMessage(msg, () -> {});
                });
            }
        }
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView messageUser;
        public TextView messageTime;
        public TextView messageText;
        public ImageView messageImg;
        public ImageView editBtn;
        public ImageView deleteBtn;
        public EditText editMessageText;
        public Button editSaveBtn;
        public Button editCancelBtn;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            messageUser = itemView.findViewById(R.id.chat_message_user_tv);
            messageTime = itemView.findViewById(R.id.chat_message_time_tv);
            messageText = itemView.findViewById(R.id.chat_message_text_tv);
            messageImg = itemView.findViewById(R.id.chat_message_img);
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
                messageImg.setVisibility(View.GONE);
            });

            deleteBtn.setOnClickListener(v -> {
                Integer chatMessagePos = (Integer) v.getTag(R.string.messagePos);
                ChatMessage chatMessage = viewModel.getData().getValue().get(chatMessagePos);

                chatMessage.setMessageTime(new Timestamp(new Date()).getSeconds());
                chatMessage.setIsDeleted(true);

                ChatMessagesModel.instance.deleteChatMessage(chatMessage, () -> {
                    editMessageText.setVisibility(View.GONE);
                    editSaveBtn.setVisibility(View.GONE);
                    editCancelBtn.setVisibility(View.GONE);
                    messageText.setVisibility(View.VISIBLE);
                    messageImg.setVisibility(View.VISIBLE);
                });
            });

            editSaveBtn.setOnClickListener(v -> {
                Integer chatMessagePos = (Integer) v.getTag(R.string.messagePos);
                ChatMessage chatMessage = viewModel.getData().getValue().get(chatMessagePos);

                chatMessage.setMessageTime(new Timestamp(new Date()).getSeconds());
                chatMessage.setTextMessage(editMessageText.getText().toString());

                ChatMessagesModel.instance.updateChatMessage(chatMessage, () -> {
                    editMessageText.setVisibility(View.GONE);
                    editSaveBtn.setVisibility(View.GONE);
                    editCancelBtn.setVisibility(View.GONE);
                    messageText.setVisibility(View.VISIBLE);
                    messageImg.setVisibility(View.VISIBLE);
                });
            });

            editCancelBtn.setOnClickListener(v -> {
                editMessageText.setVisibility(View.GONE);
                editSaveBtn.setVisibility(View.GONE);
                editCancelBtn.setVisibility(View.GONE);
                messageText.setVisibility(View.VISIBLE);
                messageImg.setVisibility(View.VISIBLE);
            });
        }
    }

    private void upload() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
        galleryIntent.setType("image/*");

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "how would you like to get your photo");

        Intent[] intentArray = {cameraIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooser, SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    if (data.getData() != null) {
                        filePath = data.getData();
                        photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    } else {
                        photo = (Bitmap) data.getExtras().get("data");
                    }

                    photoIndicatiomImg.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.fragment_chat_message, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ChatMessage chatMessage = viewModel.getData().getValue().get(position);

            holder.editSaveBtn.setTag(R.string.messagePos, position);
            holder.deleteBtn.setTag(R.string.messagePos, position);
            holder.messageText.setText(chatMessage.getTextMessage());
            holder.messageUser.setText(chatMessage.getSendingId());
            holder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getMessageTime() * 1000));

            if (chatMessage.getSendingId().equals(sendingPetId)) {
                holder.editBtn.setVisibility(View.VISIBLE);
                holder.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.editBtn.setVisibility(View.GONE);
                holder.deleteBtn.setVisibility(View.GONE);
            }

            String img = chatMessage.getImgUrl();
            Picasso.get().load(img).into(holder.messageImg);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null) {
                return 0;
            }
            return viewModel.getData().getValue().size();
        }
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}