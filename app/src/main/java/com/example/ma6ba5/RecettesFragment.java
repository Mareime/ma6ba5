package com.example.ma6ba5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// RecettesFragment.java
public class RecettesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecetteAdapter adapter;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recettes, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        loadRecettes();

        return view;
    }

    private void loadRecettes() {
        apiService.getAllRecettes().enqueue(new Callback<List<Recette>>() {
            @Override
            public void onResponse(Call<List<Recette>> call, Response<List<Recette>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new RecetteAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Recette>> call, Throwable t) {
                Toast.makeText(getContext(), "Échec de chargement des recettes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}