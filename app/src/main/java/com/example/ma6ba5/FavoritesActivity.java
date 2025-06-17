package com.example.ma6ba5;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recettesRecyclerView, boissonsRecyclerView;
    private RecetteAdapter recetteAdapter;
    private BoissonAdapter boissonAdapter;
    private ApiService apiService;
    private TextView emptyFavoritesTextView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize views
        recettesRecyclerView = findViewById(R.id.recettesRecyclerView);
        boissonsRecyclerView = findViewById(R.id.boissonsRecyclerView);
        emptyFavoritesTextView = findViewById(R.id.emptyFavoritesTextView);
        backButton = findViewById(R.id.backButton);

        // Setup back button with proper navigation
        backButton.setOnClickListener(v -> {
            // Close this activity and return to MainActivity
            finish();
        });

        // Setup RecyclerViews
        recettesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        boissonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Load data
        loadFavoriteRecettes();
        loadFavoriteBoissons();
    }

    // Handle system back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // Rest of the methods remain the same...
    private void loadFavoriteRecettes() {
        apiService.getAllRecettes().enqueue(new Callback<List<Recette>>() {
            @Override
            public void onResponse(Call<List<Recette>> call, Response<List<Recette>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recette> allRecettes = response.body();
                    List<Recette> favoriteRecettes = new ArrayList<>();

                    for (Recette recette : allRecettes) {
                        if (recette.isPreferer()) {
                            favoriteRecettes.add(recette);
                        }
                    }

                    recetteAdapter = new RecetteAdapter(favoriteRecettes);
                    recettesRecyclerView.setAdapter(recetteAdapter);

                    checkIfEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<Recette>> call, Throwable t) {
                Toast.makeText(FavoritesActivity.this,
                        "Échec de chargement des recettes favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFavoriteBoissons() {
        apiService.getAllBoissons().enqueue(new Callback<List<Boisson>>() {
            @Override
            public void onResponse(Call<List<Boisson>> call, Response<List<Boisson>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Boisson> allBoissons = response.body();
                    List<Boisson> favoriteBoissons = new ArrayList<>();

                    for (Boisson boisson : allBoissons) {
                        if (boisson.isPreferer()) {
                            favoriteBoissons.add(boisson);
                        }
                    }

                    boissonAdapter = new BoissonAdapter(favoriteBoissons);
                    boissonsRecyclerView.setAdapter(boissonAdapter);

                    checkIfEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<Boisson>> call, Throwable t) {
                Toast.makeText(FavoritesActivity.this,
                        "Échec de chargement des boissons favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfEmpty() {
        boolean hasRecettes = recetteAdapter != null && recetteAdapter.getItemCount() > 0;
        boolean hasBoissons = boissonAdapter != null && boissonAdapter.getItemCount() > 0;

        if (emptyFavoritesTextView != null) {
            if (!hasRecettes && !hasBoissons) {
                emptyFavoritesTextView.setVisibility(android.view.View.VISIBLE);
            } else {
                emptyFavoritesTextView.setVisibility(android.view.View.GONE);
            }
        }
    }
}