package com.example.ma6ba5;

import android.os.Bundle;
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

public class BoissonActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTextView, ingredientsTextView, preparationTextView, preparationTimeTextView;
    private ImageButton favoriteButton;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private EditText commentEditText;
    private Button submitCommentButton;

    private ApiService apiService;
    private Long boissonId;
    private Boisson boisson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boisson_detail);

        // Initialize views
        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.nameTextView);
        ingredientsTextView = findViewById(R.id.ingredientsTextView);
        preparationTextView = findViewById(R.id.preparationTextView);
        preparationTimeTextView = findViewById(R.id.preparationTimeTextView);
        favoriteButton = findViewById(R.id.favoriteButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        boissonId = getIntent().getLongExtra("boisson_id", -1);
        if (boissonId == -1) {
            finish();
            return;
        }

        // Setup recycler view
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load boisson details
        loadBoissonDetails();

        // Load comments
        loadComments();

        // Setup favorite button
        favoriteButton.setOnClickListener(v -> toggleFavorite());

        // Setup comment submission
        submitCommentButton.setOnClickListener(v -> submitComment());
    }

    private void loadBoissonDetails() {
        apiService.getBoissonById(boissonId).enqueue(new Callback<Boisson>() {
            @Override
            public void onResponse(Call<Boisson> call, Response<Boisson> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boisson = response.body();

                    nameTextView.setText(boisson.getNom());
                    ingredientsTextView.setText(boisson.getIngredients());
                    preparationTextView.setText(boisson.getPreparation());
                    preparationTimeTextView.setText(getString(R.string.duration_format, boisson.getTempsPreparation()));

                    updateFavoriteIcon();

                    if (boisson.getImageUrl() != null && !boisson.getImageUrl().isEmpty()) {
                        Glide.with(BoissonActivity.this)
                                .load(boisson.getImageUrl())
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.error_image)
                                .into(imageView);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boisson> call, Throwable t) {
                Toast.makeText(BoissonActivity.this,
                        "Échec de chargement des détails", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComments() {
        apiService.getBoissonCommentaires(boissonId).enqueue(new Callback<List<Commentaire>>() {
            @Override
            public void onResponse(Call<List<Commentaire>> call, Response<List<Commentaire>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentAdapter = new CommentAdapter(response.body());
                    commentsRecyclerView.setAdapter(commentAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Commentaire>> call, Throwable t) {
                Toast.makeText(BoissonActivity.this,
                        "Échec de chargement des commentaires", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavorite() {
        if (boisson != null) {
            apiService.toggleBoissonFavorite(boissonId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Toggle the state locally
                        boisson.setPreferer(!boisson.isPreferer());
                        updateFavoriteIcon();

                        String message = boisson.isPreferer() ?
                                "Ajouté aux favoris" : "Retiré des favoris";
                        Toast.makeText(BoissonActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(BoissonActivity.this,
                            "Échec de mise à jour des favoris", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateFavoriteIcon() {
        favoriteButton.setImageResource(boisson.isPreferer() ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }

    private void submitComment() {
        String commentText = commentEditText.getText().toString().trim();
        if (commentText.isEmpty()) {
            return;
        }

        Commentaire comment = new Commentaire();
        comment.setContenu(commentText);
        comment.setTypeCible(Commentaire.CibleType.BOISSON);
        comment.setIdCible(boissonId);

        apiService.addCommentaire(comment).enqueue(new Callback<Commentaire>() {
            @Override
            public void onResponse(Call<Commentaire> call, Response<Commentaire> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentEditText.setText("");

                    // Refresh comments
                    loadComments();

                    Toast.makeText(BoissonActivity.this,
                            "Commentaire ajouté", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Commentaire> call, Throwable t) {
                Toast.makeText(BoissonActivity.this,
                        "Échec d'ajout du commentaire", Toast.LENGTH_SHORT).show();
            }
        });
    }
}