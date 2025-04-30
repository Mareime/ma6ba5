package com.example.ma6ba5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoriqueActivity extends AppCompatActivity implements RecetteAdapter.OnRecetteListener {

    private RecyclerView recyclerView;
    private RecetteAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView tvAucunHistorique;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        
        // Initialiser les vues
        recyclerView = findViewById(R.id.rv_historique);
        tvAucunHistorique = findViewById(R.id.tv_aucun_historique);
        
        // Configurer la RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialiser la base de données
        dbHelper = new DatabaseHelper(this);
        
        // Charger l'historique
        chargerHistorique();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_historique, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_vider_historique) {
            // Afficher une boîte de dialogue de confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.confirmer);
            builder.setMessage(R.string.confirmer_vider_historique);
            builder.setPositiveButton(R.string.oui, (dialog, which) -> {
                // Vider l'historique
                dbHelper.viderHistorique();
                chargerHistorique();
            });
            builder.setNegativeButton(R.string.non, null);
            builder.show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void chargerHistorique() {
        List<Recette> recettesHistorique = dbHelper.getHistoriqueRecettes(50); // Limiter à 50 recettes
        
        if (recettesHistorique.isEmpty()) {
            tvAucunHistorique.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvAucunHistorique.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            
            if (adapter == null) {
                adapter = new RecetteAdapter(this, recettesHistorique, this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(recettesHistorique);
            }
        }
    }
    
    @Override
    public void onRecetteClick(int position) {
        // Récupérer la recette sélectionnée
        List<Recette> recettesHistorique = dbHelper.getHistoriqueRecettes(50);
        Recette recetteSelectionnee = recettesHistorique.get(position);
        
        // Ajouter à l'historique à nouveau (pour mettre à jour la date)
        dbHelper.ajouterHistorique(recetteSelectionnee.getId());
        
        // Ouvrir l'activité de détail de recette
        Intent intent = new Intent(this, DetailRecetteActivity.class);
        intent.putExtra("RECETTE_ID", recetteSelectionnee.getId());
        startActivity(intent);
    }
}