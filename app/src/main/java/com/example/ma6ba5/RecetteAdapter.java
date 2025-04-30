package com.example.ma6ba5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecetteAdapter extends RecyclerView.Adapter<RecetteAdapter.RecetteViewHolder> {
    
    private Context context;
    private List<Recette> recettes;
    private OnRecetteListener onRecetteListener;
    private DatabaseHelper dbHelper;
    
    public RecetteAdapter(Context context, List<Recette> recettes, OnRecetteListener onRecetteListener) {
        this.context = context;
        this.recettes = recettes;
        this.onRecetteListener = onRecetteListener;
        this.dbHelper = new DatabaseHelper(context);
    }
    
    @NonNull
    @Override
    public RecetteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recette, parent, false);
        return new RecetteViewHolder(view, onRecetteListener);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecetteViewHolder holder, int position) {
        Recette recette = recettes.get(position);
        
        // Afficher les données de la recette
        holder.tvTitre.setText(recette.getTitre());
        holder.tvDuree.setText(context.getString(R.string.duree_minutes, recette.getDuree()));
        holder.tvTypePlat.setText(recette.getTypePlat());
        
        // Charger l'image avec Glide
        if (recette.getImage() != null && !recette.getImage().isEmpty()) {
            Glide.with(context)
                    .load(recette.getImage())
                    .placeholder(R.drawable.placeholder_recette)
                    .error(R.drawable.placeholder_recette)
                    .centerCrop()
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.placeholder_recette);
        }
        
        // Mettre à jour l'icône de favori
        boolean estFavori = dbHelper.estFavori(recette.getId());
        if (estFavori) {
            holder.btnFavori.setImageResource(R.drawable.ic_favori_plein);
        } else {
            holder.btnFavori.setImageResource(R.drawable.ic_favori_vide);
        }
        
        // Gérer le clic sur le bouton favori
        holder.btnFavori.setOnClickListener(v -> {
            if (dbHelper.estFavori(recette.getId())) {
                // Supprimer des favoris
                dbHelper.supprimerFavori(recette.getId());
                holder.btnFavori.setImageResource(R.drawable.ic_favori_vide);
            } else {
                // Ajouter aux favoris
                dbHelper.ajouterFavori(recette.getId());
                holder.btnFavori.setImageResource(R.drawable.ic_favori_plein);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return recettes.size();
    }
    
    public void updateData(List<Recette> nouvelleListe) {
        this.recettes = nouvelleListe;
        notifyDataSetChanged();
    }
    
    static class RecetteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivImage;
        TextView tvTitre, tvDuree, tvTypePlat;
        ImageButton btnFavori;
        OnRecetteListener onRecetteListener;
        
        public RecetteViewHolder(@NonNull View itemView, OnRecetteListener onRecetteListener) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_recette_image);
            tvTitre = itemView.findViewById(R.id.tv_recette_titre);
            tvDuree = itemView.findViewById(R.id.tv_recette_duree);
            tvTypePlat = itemView.findViewById(R.id.tv_recette_type);
            btnFavori = itemView.findViewById(R.id.btn_favori);
            
            this.onRecetteListener = onRecetteListener;
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            onRecetteListener.onRecetteClick(getAdapterPosition());
        }
    }
    
    public interface OnRecetteListener {
        void onRecetteClick(int position);
    }
}