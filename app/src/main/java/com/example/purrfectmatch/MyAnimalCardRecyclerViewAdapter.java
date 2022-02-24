package com.example.purrfectmatch;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.purrfectmatch.databinding.FragmentAnimalCardBinding;
import com.example.purrfectmatch.model.Pet;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.example.purrfectmatch.model.Pet}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAnimalCardRecyclerViewAdapter extends RecyclerView.Adapter<MyAnimalCardRecyclerViewAdapter.ViewHolder> {

    private final List<Pet> pets;

    public MyAnimalCardRecyclerViewAdapter(List<Pet> items) {
        pets = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentAnimalCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) { // TODO: Add onclicks etc
        holder.mItem = pets.get(position);
        holder.petName.setText(pets.get(position).getName());
        Picasso.get().load(pets.get(position).getPetUrl()).into(holder.petImage);
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView petName;
        public final ImageView petImage;
        public Pet mItem;

        public ViewHolder(FragmentAnimalCardBinding binding) {
            super(binding.getRoot());
            petName = binding.pcPetName;
            petImage = binding.pcPetImage;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + petName.getText() + "'";
        }
    }
}