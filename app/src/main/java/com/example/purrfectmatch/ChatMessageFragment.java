package com.example.purrfectmatch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatMessageFragment extends Fragment {
    TextView messageUser;
    TextView messageTime;
    TextView messageText;
    ImageView editBtn;
    ImageView deleteBtn;
    EditText editMessageText;
    Button editSaveBtn;
    Button editCancelBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_message, container, false);

        messageUser = view.findViewById(R.id.chat_message_user_tv);
        messageTime = view.findViewById(R.id.chat_message_time_tv);
        messageText = view.findViewById(R.id.chat_message_text_tv);
        editBtn = view.findViewById(R.id.chat_message_edit_btn);
        deleteBtn = view.findViewById(R.id.chat_message_delete_btn);
        editMessageText = view.findViewById(R.id.chat_message_text_et);
        editSaveBtn = view.findViewById(R.id.chat_message_edit_save_btn);
        editCancelBtn = view.findViewById(R.id.chat_message_edit_cancel_btn);

        // TODO: Remove that in integration with chatFragmaent
        messageUser.setText("yuval");
        messageTime.setText("05/03/2022 22:27");
        messageText.setText("hiiii :)");

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

        return view;
    }
}