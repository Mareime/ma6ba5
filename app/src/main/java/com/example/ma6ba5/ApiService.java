//package com.example.ma6ba5;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.PATCH;
//import retrofit2.http.POST;
//import retrofit2.http.PUT;
//import retrofit2.http.Path;
//
//public interface ApiService {
//    // Recette endpoints
//    @GET("/api/recettes")
//    Call<List<Recette>> getAllRecettes();
//
//    @GET("/api/recettes/{id}")
//    Call<Recette> getRecetteById(@Path("id") Long id);
//
//    @PUT("/api/recettes/{id}/favorite")
//    Call<Recette> toggleRecetteFavorite(@Path("id") Long id, @Body Recette recette);
//
//    // Boisson endpoints
//    @GET("/api/boissons")
//    Call<List<Boisson>> getAllBoissons();
//
//    @GET("/api/boissons/{id}")
//    Call<Boisson> getBoissonById(@Path("id") Long id);
//
//    @PATCH("/api/boissons/{id}/favorite")
//    Call<Void> toggleBoissonFavorite(@Path("id") Long id);
//
//    // Commentaire endpoints
//    @GET("/api/commentaires/cible/recette/{id}")
//    Call<List<Commentaire>> getRecetteCommentaires(@Path("id") Long id);
//
//    @GET("/api/commentaires/cible/boisson/{id}")
//    Call<List<Commentaire>> getBoissonCommentaires(@Path("id") Long id);
//
//    @POST("/api/commentaires")
//    Call<Commentaire> addCommentaire(@Body Commentaire commentaire);
//}
// ApiService.java - CORRECTED VERSION
package com.example.ma6ba5;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // Recette endpoints
    @GET("/api/recettes")
    Call<List<Recette>> getAllRecettes();

    @GET("/api/recettes/{id}")
    Call<Recette> getRecetteById(@Path("id") Long id);

    // FIXED: Changed to PATCH and no body parameter (like boisson)
    @PATCH("/api/recettes/{id}/favorite")
    Call<Void> toggleRecetteFavorite(@Path("id") Long id);

    // Boisson endpoints
    @GET("/api/boissons")
    Call<List<Boisson>> getAllBoissons();

    @GET("/api/boissons/{id}")
    Call<Boisson> getBoissonById(@Path("id") Long id);

    @PATCH("/api/boissons/{id}/favorite")
    Call<Void> toggleBoissonFavorite(@Path("id") Long id);

    // Commentaire endpoints
    @GET("/api/commentaires/cible/recette/{id}")
    Call<List<Commentaire>> getRecetteCommentaires(@Path("id") Long id);

    @GET("/api/commentaires/cible/boisson/{id}")
    Call<List<Commentaire>> getBoissonCommentaires(@Path("id") Long id);

    @POST("/api/commentaires")
    Call<Commentaire> addCommentaire(@Body Commentaire commentaire);
}