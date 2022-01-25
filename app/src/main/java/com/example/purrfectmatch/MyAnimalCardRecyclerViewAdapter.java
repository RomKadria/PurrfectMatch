package com.example.purrfectmatch;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.purrfectmatch.databinding.FragmentAnimalCardBinding;
import com.example.purrfectmatch.model.Pet;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.example.purrfectmatch.model.Pet}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAnimalCardRecyclerViewAdapter extends RecyclerView.Adapter<MyAnimalCardRecyclerViewAdapter.ViewHolder> {

    private final List<Pet> mValues;

    public MyAnimalCardRecyclerViewAdapter(List<Pet> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentAnimalCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) { // TODO: Add onclicks etc
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getDescription());
        holder.petImage.setImageURI(mValues.get(position).getPetUrl());  // TODO: get the actual image
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public ImageView petImage;
        public Pet mItem;

        public ViewHolder(FragmentAnimalCardBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}