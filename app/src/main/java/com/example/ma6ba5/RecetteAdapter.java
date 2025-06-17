package com.example.ma6ba5;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecetteAdapter extends RecyclerView.Adapter<RecetteAdapter.RecetteViewHolder> {
    private List<Recette> recettes;

    public RecetteAdapter(List<Recette> recettes) {
        this.recettes = recettes;
    }

    @NonNull
    @Override
    public RecetteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recette, parent, false);
        return new RecetteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetteViewHolder holder, int position) {
        Recette recette = recettes.get(position);

        holder.titleTextView.setText(recette.getTitre());
        holder.durationTextView.setText(holder.itemView.getContext().getString(R.string.duration_format, recette.getDuree()));

        // Load image with Glide - IMPROVED with placeholder and error handling
        if (recette.getImageUrl() != null && !recette.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(recette.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image);
        }

        // Set favorite icon state
        holder.favoriteIcon.setImageResource(recette.isPreferer() ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        // Set click listener for the whole item
        // In RecetteAdapter, modify the click listener:
        holder.itemView.setOnClickListener(v -> {
            try {
                Log.d("RecetteAdapter", "Clicking recette with ID: " + recette.getId());
                Intent intent = new Intent(holder.itemView.getContext(), RecetteDetailActivity.class);
                intent.putExtra("recette_id", recette.getId());
                holder.itemView.getContext().startActivity(intent);
            } catch (Exception e) {
                Log.e("RecetteAdapter", "Error starting activity: " + e.getMessage(), e);
                Toast.makeText(holder.itemView.getContext(),
                        "Erreur lors de l'ouverture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recettes != null ? recettes.size() : 0;
    }

    static class RecetteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView durationTextView;
        ImageView favoriteIcon;

        public RecetteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
        }
    }
}