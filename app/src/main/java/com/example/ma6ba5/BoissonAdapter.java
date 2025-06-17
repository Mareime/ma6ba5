//package com.example.ma6ba5;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//public class BoissonAdapter extends RecyclerView.Adapter<BoissonAdapter.BoissonViewHolder> {
//    // Similar implementation to RecetteAdapter but for Boisson objects
//}

package com.example.ma6ba5;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BoissonAdapter extends RecyclerView.Adapter<BoissonAdapter.BoissonViewHolder> {
    private List<Boisson> boissons;

    public BoissonAdapter(List<Boisson> boissons) {
        this.boissons = boissons;
    }

    @NonNull
    @Override
    public BoissonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boisson, parent, false);
        return new BoissonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoissonViewHolder holder, int position) {
        Boisson boisson = boissons.get(position);

        holder.nameTextView.setText(boisson.getNom());
        holder.preparationTimeTextView.setText(holder.itemView.getContext().getString(R.string.duration_format, boisson.getTempsPreparation()));

        // Load image with Glide
        if (boisson.getImageUrl() != null && !boisson.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(boisson.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image);
        }

        // Set favorite icon state
        holder.favoriteIcon.setImageResource(boisson.isPreferer() ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        // Set click listener for the whole item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), BoissonActivity.class);
            intent.putExtra("boisson_id", boisson.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return boissons.size();
    }

    static class BoissonViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView preparationTimeTextView;
        ImageView favoriteIcon;

        public BoissonViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            preparationTimeTextView = itemView.findViewById(R.id.preparationTimeTextView);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
        }
    }
}