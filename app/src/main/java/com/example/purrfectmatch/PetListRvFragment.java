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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;
import com.example.purrfectmatch.model.SaveSharedPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class PetListRvFragment extends Fragment {
    PetListRvViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PetListRvViewModel.class);
    }

    // create an action bar button
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_menu, menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout_btn) {
            SaveSharedPreference.clearAll(this.getActivity().getApplicationContext());
            Navigation.findNavController(this.getView())
                    .navigate(PetListRvFragmentDirections
                            .actionPetListRvFragmentToLoginFragment());
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_list,container,false);

        setHasOptionsMenu(true);

        swipeRefresh = view.findViewById(R.id.petlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshPetList());

        FloatingActionButton watchMap = view.findViewById(R.id.petlist_watchMap_fb);
        watchMap.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(PetListRvFragmentDirections.actionPetListRvFragmentToAllLocationsMapFragment(null));
        });

        RecyclerView list = view.findViewById(R.id.petlist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                String petId = viewModel.getData().getValue().get(position).getEmail();

               Navigation.findNavController(v).navigate(PetListRvFragmentDirections.actionPetListRvFragmentToPetDetailsFragment(petId));
            }
        });

        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getPetListLoadingState().getValue() == Model.PetListLoadingState.loading);
        Model.instance.getPetListLoadingState().observe(getViewLifecycleOwner(), PetListLoadingState -> {
            if (PetListLoadingState == Model.PetListLoadingState.loading){
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

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.pet_list_card,parent,false);
            MyViewHolder holder = new MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Pet pet = viewModel.getData().getValue().get(position);
            holder.petName.setText(pet.getName());
            Picasso.get().load(pet.getPetUrl()).into(holder.petImage);
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