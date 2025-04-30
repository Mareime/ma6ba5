package com.example.ma6ba5;

import java.io.Serializable;

public class Recette implements Serializable {
    private long id;
    private String titre;
    private String ingredients;
    private String etapes;
    private int duree;
    private String image;
    private String typePlat;
    private boolean estFavori;

    // Constructeur par défaut
    public Recette() {
    }

    // Constructeur complet
    public Recette(long id, String titre, String ingredients, String etapes, int duree, String image, String typePlat) {
        this.id = id;
        this.titre = titre;
        this.ingredients = ingredients;
        this.etapes = etapes;
        this.duree = duree;
        this.image = image;
        this.typePlat = typePlat;
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getEtapes() {
        return etapes;
    }

    public void setEtapes(String etapes) {
        this.etapes = etapes;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypePlat() {
        return typePlat;
    }

    public void setTypePlat(String typePlat) {
        this.typePlat = typePlat;
    }

    public boolean isEstFavori() {
        return estFavori;
    }

    public void setEstFavori(boolean estFavori) {
        this.estFavori = estFavori;
    }

    @Override
    public String toString() {
        return titre;
    }
}