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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.purrfectmatch.model.ChatMessage;
import com.example.purrfectmatch.model.ChatMessagesModel;
import com.squareup.picasso.Picasso;

public class ChatFragment extends Fragment {
    ChatMessagesViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ChatMessagesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        swipeRefresh = view.findViewById(R.id.chat_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> ChatMessagesModel.instance.refreshChatMessages());

        RecyclerView list = view.findViewById(R.id.list_of_messages);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
               // TODO: move to chat
            }
        });

        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(ChatMessagesModel.instance.getChatMessagesLoadingState().getValue() == ChatMessagesModel.LoadingState.loading);
        ChatMessagesModel.instance.getChatMessagesLoadingState().observe(getViewLifecycleOwner(), chatMessagesLoadingState -> {
            if (chatMessagesLoadingState == ChatMessagesModel.LoadingState.loading){
                swipeRefresh.setRefreshing(true);
            }else{
                swipeRefresh.setRefreshing(false);
            }

        });
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView petName;
        public ImageView petImage;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            petName = itemView.findViewById(R.id.pc_pet_name); // TODO: change this
            petImage = itemView.findViewById(R.id.pc_pet_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v,pos);
                }
            });
        }
    }

    interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.fragment_chat_message,parent,false);
            MyViewHolder holder = new MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ChatMessage chatMessage = viewModel.getData().getValue().get(position);
            holder.petName.setText(chatMessage.getName()); // TODO: change this
            Picasso.get().load(chatMessage.getPetUrl()).into(holder.petImage);
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData().getValue() == null){
                return 0;
            }
            return viewModel.getData().getValue().size();
        }
    }
}