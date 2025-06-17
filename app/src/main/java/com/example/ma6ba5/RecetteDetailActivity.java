// Updated RecetteDetailActivity.java with better error handling
package com.example.ma6ba5;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecetteDetailActivity extends AppCompatActivity {
    private static final String TAG = "RecetteDetailActivity";

    private ImageView imageView;
    private TextView titleTextView, ingredientsTextView, etapesTextView, dureeTextView;
    private ImageButton favoriteButton;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private EditText commentEditText;
    private Button submitCommentButton;

    private ApiService apiService;
    private Long recetteId;
    private Recette recette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_recette_detail);
            Log.d(TAG, "Layout inflated successfully");

            // Initialize views with null checks
            initializeViews();

            // Initialize API service
            apiService = RetrofitClient.getClient().create(ApiService.class);

            // Get recette ID from intent
            recetteId = getIntent().getLongExtra("recette_id", -1);
            Log.d(TAG, "Recette ID: " + recetteId);

            if (recetteId == -1) {
                Toast.makeText(this, "Erreur: ID de recette invalide", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Setup recycler view
            if (commentsRecyclerView != null) {
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

            // Load data
            loadRecetteDetails();
            loadComments();

            // Setup click listeners
            setupClickListeners();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Erreur lors du chargement de la page", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        try {
            imageView = findViewById(R.id.imageView);
            titleTextView = findViewById(R.id.titleTextView);
            ingredientsTextView = findViewById(R.id.ingredientsTextView);
            etapesTextView = findViewById(R.id.etapesTextView);
            dureeTextView = findViewById(R.id.dureeTextView);
            favoriteButton = findViewById(R.id.favoriteButton);
            commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
            commentEditText = findViewById(R.id.commentEditText);
            submitCommentButton = findViewById(R.id.submitCommentButton);

            Log.d(TAG, "All views initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage(), e);
        }
    }

    private void setupClickListeners() {
        try {
            if (favoriteButton != null) {
                favoriteButton.setOnClickListener(v -> toggleFavorite());
            }

            if (submitCommentButton != null) {
                submitCommentButton.setOnClickListener(v -> submitComment());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners: " + e.getMessage(), e);
        }
    }

    private void loadRecetteDetails() {
        try {
            apiService.getRecetteById(recetteId).enqueue(new Callback<Recette>() {
                @Override
                public void onResponse(Call<Recette> call, Response<Recette> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            recette = response.body();
                            Log.d(TAG, "Recette loaded: " + recette.getTitre());
                            populateViews();
                        } else {
                            Log.e(TAG, "Error loading recette: " + response.code());
                            Toast.makeText(RecetteDetailActivity.this,
                                    "Erreur de chargement: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error in onResponse: " + e.getMessage(), e);
                    }
                }

                @Override
                public void onFailure(Call<Recette> call, Throwable t) {
                    Log.e(TAG, "Network error: " + t.getMessage(), t);
                    Toast.makeText(RecetteDetailActivity.this,
                            "Échec de chargement des détails: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in loadRecetteDetails: " + e.getMessage(), e);
        }
    }

    private void populateViews() {
        try {
            if (recette == null) {
                Log.w(TAG, "Recette is null, cannot populate views");
                return;
            }

            if (titleTextView != null) {
                titleTextView.setText(recette.getTitre());
            }

            if (ingredientsTextView != null) {
                ingredientsTextView.setText(recette.getIngredients());
            }

            if (etapesTextView != null) {
                etapesTextView.setText(recette.getEtapes());
            }

            if (dureeTextView != null) {
                dureeTextView.setText(getString(R.string.duration_format, recette.getDuree()));
            }

            updateFavoriteIcon();

            // Load image with Glide
            if (imageView != null && recette.getImageUrl() != null && !recette.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(recette.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imageView);
            }

            Log.d(TAG, "Views populated successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error populating views: " + e.getMessage(), e);
        }
    }

    private void loadComments() {
        try {
            apiService.getRecetteCommentaires(recetteId).enqueue(new Callback<List<Commentaire>>() {
                @Override
                public void onResponse(Call<List<Commentaire>> call, Response<List<Commentaire>> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null && commentsRecyclerView != null) {
                            commentAdapter = new CommentAdapter(response.body());
                            commentsRecyclerView.setAdapter(commentAdapter);
                            Log.d(TAG, "Comments loaded: " + response.body().size());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error in comments onResponse: " + e.getMessage(), e);
                    }
                }

                @Override
                public void onFailure(Call<List<Commentaire>> call, Throwable t) {
                    Log.e(TAG, "Comments network error: " + t.getMessage(), t);
                    Toast.makeText(RecetteDetailActivity.this,
                            "Échec de chargement des commentaires", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in loadComments: " + e.getMessage(), e);
        }
    }

    private void toggleFavorite() {
        try {
            if (recette != null) {
                apiService.toggleRecetteFavorite(recetteId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        try {
                            if (response.isSuccessful()) {
                                recette.setPreferer(!recette.isPreferer());
                                updateFavoriteIcon();

                                String message = recette.isPreferer() ?
                                        "Ajouté aux favoris" : "Retiré des favoris";
                                Toast.makeText(RecetteDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RecetteDetailActivity.this,
                                        "Échec de mise à jour des favoris", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error in favorite toggle response: " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Favorite toggle error: " + t.getMessage(), t);
                        Toast.makeText(RecetteDetailActivity.this,
                                "Échec de mise à jour des favoris", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in toggleFavorite: " + e.getMessage(), e);
        }
    }

    private void updateFavoriteIcon() {
        try {
            if (recette != null && favoriteButton != null) {
                // Using system icons as fallback
                favoriteButton.setImageResource(recette.isPreferer() ?
                        R.drawable.ic_favorite : R.drawable.ic_favorite_border);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating favorite icon: " + e.getMessage(), e);
        }
    }

    private void submitComment() {
        try {
            if (commentEditText == null) return;

            String commentText = commentEditText.getText().toString().trim();
            if (commentText.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir un commentaire", Toast.LENGTH_SHORT).show();
                return;
            }

            Commentaire comment = new Commentaire();
            comment.setContenu(commentText);
            comment.setTypeCible(Commentaire.CibleType.RECETTE);
            comment.setIdCible(recetteId);

            apiService.addCommentaire(comment).enqueue(new Callback<Commentaire>() {
                @Override
                public void onResponse(Call<Commentaire> call, Response<Commentaire> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            commentEditText.setText("");
                            loadComments(); // Refresh comments
                            Toast.makeText(RecetteDetailActivity.this,
                                    "Commentaire ajouté", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error in comment submit response: " + e.getMessage(), e);
                    }
                }

                @Override
                public void onFailure(Call<Commentaire> call, Throwable t) {
                    Log.e(TAG, "Comment submit error: " + t.getMessage(), t);
                    Toast.makeText(RecetteDetailActivity.this,
                            "Échec d'ajout du commentaire", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in submitComment: " + e.getMessage(), e);
        }
    }
}