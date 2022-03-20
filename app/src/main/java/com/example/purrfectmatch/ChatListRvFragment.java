package com.example.purrfectmatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.purrfectmatch.model.ChatMessage;
import com.example.purrfectmatch.model.ChatPet;
import com.example.purrfectmatch.model.ChatsModel;
import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatListRvFragment extends Fragment {

    ChatListRvViewModel viewModel;
    ChatListRvFragment.MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    String petId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ChatListRvViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list,container,false);
        petId = ChatListRvFragmentArgs.fromBundle(getArguments()).getPetId();

        swipeRefresh = view.findViewById(R.id.chatlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> ChatsModel.instance.refreshChatsList(petId));

        RecyclerView list = view.findViewById(R.id.chatlist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChatListRvFragment.MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChatListRvFragment.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                String selectedPetId = viewModel.getData(petId).getValue().get(position).getEmail();
                Navigation.findNavController(v).navigate(ChatListRvFragmentDirections.actionChatListRvFragmentToChatFragment(petId, selectedPetId));
            }
        });

        setHasOptionsMenu(true);
        viewModel.getData(petId).observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(ChatsModel.instance.getChatPetsListLoadingState().getValue() == ChatsModel.LoadingState.loading);
        ChatsModel.instance.getChatPetsListLoadingState().observe(getViewLifecycleOwner(), ChatPetsListLoadingState -> {
            if (ChatPetsListLoadingState == ChatsModel.LoadingState.loading){
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

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView petName;
        public ImageView petImage;

        public MyViewHolder(@NonNull View itemView, ChatListRvFragment.OnItemClickListener listener) {
            super(itemView);
            petName = itemView.findViewById(R.id.pc_pet_name);
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

    class MyAdapter extends RecyclerView.Adapter<ChatListRvFragment.MyViewHolder> {

        ChatListRvFragment.OnItemClickListener listener;

        public void setOnItemClickListener(ChatListRvFragment.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public ChatListRvFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.pet_list_card, parent, false);
            ChatListRvFragment.MyViewHolder holder = new ChatListRvFragment.MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChatListRvFragment.MyViewHolder holder, int position) {
            ChatPet chatPet = viewModel.getData(petId).getValue().get(position);
            holder.petName.setText(chatPet.getName());
            Picasso.get().load(chatPet.getPetUrl()).into(holder.petImage);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData(petId).getValue() == null) {
                return 0;
            }
            return viewModel.getData(petId).getValue().size();
        }
    }
}