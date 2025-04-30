package com.example.ma6ba5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavorisActivity extends AppCompatActivity implements RecetteAdapter.OnRecetteListener {

    private RecyclerView recyclerView;
    private RecetteAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView tvAucunFavori;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        
        // Initialiser les vues
        recyclerView = findViewById(R.id.rv_favoris);
        tvAucunFavori = findViewById(R.id.tv_aucun_favori);
        
        // Configurer la RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialiser la base de données
        dbHelper = new DatabaseHelper(this);
        
        // Charger les favoris
        chargerFavoris();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Recharger les favoris à chaque fois que l'activité devient visible
        chargerFavoris();
    }
    
    private void chargerFavoris() {
        List<Recette> recettesFavorites = dbHelper.getRecettesFavorites();
        
        if (recettesFavorites.isEmpty()) {
            tvAucunFavori.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvAucunFavori.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            
            if (adapter == null) {
                adapter = new RecetteAdapter(this, recettesFavorites, this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(recettesFavorites);
            }
        }
    }
    
    @Override
    public void onRecetteClick(int position) {
        // Récupérer la recette sélectionnée
        List<Recette> recettesFavorites = dbHelper.getRecettesFavorites();
        Recette recetteSelectionnee = recettesFavorites.get(position);
        
        // Ajouter à l'historique
        dbHelper.ajouterHistorique(recetteSelectionnee.getId());
        
        // Ouvrir l'activité de détail de recette
        Intent intent = new Intent(this, DetailRecetteActivity.class);
        intent.putExtra("RECETTE_ID", recetteSelectionnee.getId());
        startActivity(intent);
    }
}