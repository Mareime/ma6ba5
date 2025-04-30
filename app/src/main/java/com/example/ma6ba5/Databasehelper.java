package com.example.ma6ba5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    
    // Nom et version de la base de données
    private static final String DATABASE_NAME = "recettes_cuisine.db";
    private static final int DATABASE_VERSION = 1;
    
    // Tables de la base de données
    public static final String TABLE_RECETTES = "recettes";
    public static final String TABLE_FAVORIS = "favoris";
    public static final String TABLE_HISTORIQUE = "historique";
    public static final String TABLE_PREFERENCES = "preferences";
    
    // Colonnes de la table recettes
    public static final String COL_ID_RECETTE = "id";
    public static final String COL_TITRE = "titre";
    public static final String COL_INGREDIENTS = "ingredients";
    public static final String COL_ETAPES = "etapes";
    public static final String COL_DUREE = "duree";
    public static final String COL_IMAGE = "image";
    public static final String COL_TYPE_PLAT = "type_plat";
    
    // Colonnes de la table favoris
    public static final String COL_ID_FAVORI = "id";
    public static final String COL_ID_RECETTE_FAVORI = "id_recette";
    public static final String COL_DATE_AJOUT_FAVORI = "date_ajout";
    
    // Colonnes de la table historique
    public static final String COL_ID_HISTORIQUE = "id";
    public static final String COL_ID_RECETTE_HISTORIQUE = "id_recette";
    public static final String COL_DATE_CONSULTATION = "date_consultation";
    
    // Colonnes de la table préférences
    public static final String COL_ID_PREFERENCE = "id";
    public static final String COL_NOM_PREFERENCE = "nom";
    public static final String COL_VALEUR_PREFERENCE = "valeur";
    
    // Création des tables
    private static final String CREATE_TABLE_RECETTES = "CREATE TABLE " + TABLE_RECETTES + "("
            + COL_ID_RECETTE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TITRE + " TEXT NOT NULL, "
            + COL_INGREDIENTS + " TEXT NOT NULL, "
            + COL_ETAPES + " TEXT NOT NULL, "
            + COL_DUREE + " INTEGER, "
            + COL_IMAGE + " TEXT, "
            + COL_TYPE_PLAT + " TEXT"
            + ")";
    
    private static final String CREATE_TABLE_FAVORIS = "CREATE TABLE " + TABLE_FAVORIS + "("
            + COL_ID_FAVORI + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ID_RECETTE_FAVORI + " INTEGER NOT NULL, "
            + COL_DATE_AJOUT_FAVORI + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(" + COL_ID_RECETTE_FAVORI + ") REFERENCES " + TABLE_RECETTES + "(" + COL_ID_RECETTE + ")"
            + ")";
    
    private static final String CREATE_TABLE_HISTORIQUE = "CREATE TABLE " + TABLE_HISTORIQUE + "("
            + COL_ID_HISTORIQUE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ID_RECETTE_HISTORIQUE + " INTEGER NOT NULL, "
            + COL_DATE_CONSULTATION + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(" + COL_ID_RECETTE_HISTORIQUE + ") REFERENCES " + TABLE_RECETTES + "(" + COL_ID_RECETTE + ")"
            + ")";
    
    private static final String CREATE_TABLE_PREFERENCES = "CREATE TABLE " + TABLE_PREFERENCES + "("
            + COL_ID_PREFERENCE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NOM_PREFERENCE + " TEXT UNIQUE NOT NULL, "
            + COL_VALEUR_PREFERENCE + " TEXT"
            + ")";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECETTES);
        db.execSQL(CREATE_TABLE_FAVORIS);
        db.execSQL(CREATE_TABLE_HISTORIQUE);
        db.execSQL(CREATE_TABLE_PREFERENCES);
        
        // Initialiser quelques préférences par défaut
        initPreferences(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En cas de mise à jour de la base de données, supprimer les anciennes tables et recréer
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORIQUE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREFERENCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECETTES);
        onCreate(db);
    }
    
    private void initPreferences(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        
        // Préférence pour le thème (clair/sombre)
        values.put(COL_NOM_PREFERENCE, "theme");
        values.put(COL_VALEUR_PREFERENCE, "clair");
        db.insert(TABLE_PREFERENCES, null, values);
        
        // Préférence pour la taille du texte
        values.clear();
        values.put(COL_NOM_PREFERENCE, "taille_texte");
        values.put(COL_VALEUR_PREFERENCE, "normale");
        db.insert(TABLE_PREFERENCES, null, values);
    }
    
    // --- Méthodes pour les favoris ---
    
    /**
     * Ajoute une recette aux favoris
     * @param idRecette ID de la recette à ajouter
     * @return ID du favori ajouté, -1 si erreur
     */
    public long ajouterFavori(long idRecette) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COL_ID_RECETTE_FAVORI, idRecette);
        
        // Vérifier si la recette est déjà dans les favoris
        if (estFavori(idRecette)) {
            return -1; // Déjà favori
        }
        
        return db.insert(TABLE_FAVORIS, null, values);
    }
    
    /**
     * Supprime une recette des favoris
     * @param idRecette ID de la recette à supprimer
     * @return Nombre de lignes affectées
     */
    public int supprimerFavori(long idRecette) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVORIS, COL_ID_RECETTE_FAVORI + " = ?", 
                new String[]{String.valueOf(idRecette)});
    }
    
    /**
     * Vérifie si une recette est dans les favoris
     * @param idRecette ID de la recette à vérifier
     * @return true si la recette est favorite, false sinon
     */
    public boolean estFavori(long idRecette) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORIS, 
                new String[]{COL_ID_FAVORI}, 
                COL_ID_RECETTE_FAVORI + " = ?", 
                new String[]{String.valueOf(idRecette)}, 
                null, null, null);
        
        boolean estFavori = cursor.getCount() > 0;
        cursor.close();
        return estFavori;
    }
    
    /**
     * Récupère toutes les recettes favorites
     * @return Liste d'IDs des recettes favorites
     */
    public List<Long> getIdsFavoris() {
        List<Long> idsFavoris = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_FAVORIS, 
                new String[]{COL_ID_RECETTE_FAVORI}, 
                null, null, null, null, 
                COL_DATE_AJOUT_FAVORI + " DESC");
        
        if (cursor.moveToFirst()) {
            do {
                idsFavoris.add(cursor.getLong(cursor.getColumnIndex(COL_ID_RECETTE_FAVORI)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return idsFavoris;
    }
    
    /**
     * Récupère toutes les recettes favorites avec leurs détails
     * @return Liste de recettes
     */
    public List<Recette> getRecettesFavorites() {
        List<Recette> recettes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT r.* FROM " + TABLE_RECETTES + " r INNER JOIN " 
                + TABLE_FAVORIS + " f ON r." + COL_ID_RECETTE 
                + " = f." + COL_ID_RECETTE_FAVORI 
                + " ORDER BY f." + COL_DATE_AJOUT_FAVORI + " DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                Recette recette = new Recette();
                recette.setId(cursor.getLong(cursor.getColumnIndex(COL_ID_RECETTE)));
                recette.setTitre(cursor.getString(cursor.getColumnIndex(COL_TITRE)));
                recette.setIngredients(cursor.getString(cursor.getColumnIndex(COL_INGREDIENTS)));
                recette.setEtapes(cursor.getString(cursor.getColumnIndex(COL_ETAPES)));
                recette.setDuree(cursor.getInt(cursor.getColumnIndex(COL_DUREE)));
                recette.setImage(cursor.getString(cursor.getColumnIndex(COL_IMAGE)));
                recette.setTypePlat(cursor.getString(cursor.getColumnIndex(COL_TYPE_PLAT)));
                recettes.add(recette);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recettes;
    }
    
    // --- Méthodes pour l'historique ---
    
    /**
     * Ajoute une recette à l'historique
     * @param idRecette ID de la recette consultée
     * @return ID de l'entrée historique ajoutée, -1 si erreur
     */
    public long ajouterHistorique(long idRecette) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COL_ID_RECETTE_HISTORIQUE, idRecette);
        
        return db.insert(TABLE_HISTORIQUE, null, values);
    }
    
    /**
     * Supprime une entrée de l'historique
     * @param idHistorique ID de l'entrée historique à supprimer
     * @return Nombre de lignes affectées
     */
    public int supprimerHistorique(long idHistorique) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HISTORIQUE, COL_ID_HISTORIQUE + " = ?", 
                new String[]{String.valueOf(idHistorique)});
    }
    
    /**
     * Vide tout l'historique
     * @return Nombre de lignes affectées
     */
    public int viderHistorique() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HISTORIQUE, null, null);
    }
    
    /**
     * Récupère l'historique des recettes consultées
     * @param limite Nombre maximum d'entrées à récupérer
     * @return Liste de recettes
     */
    public List<Recette> getHistoriqueRecettes(int limite) {
        List<Recette> recettes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT r.*, h." + COL_DATE_CONSULTATION + " FROM " 
                + TABLE_RECETTES + " r INNER JOIN " 
                + TABLE_HISTORIQUE + " h ON r." + COL_ID_RECETTE 
                + " = h." + COL_ID_RECETTE_HISTORIQUE 
                + " ORDER BY h." + COL_DATE_CONSULTATION + " DESC"
                + (limite > 0 ? " LIMIT " + limite : "");
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                Recette recette = new Recette();
                recette.setId(cursor.getLong(cursor.getColumnIndex(COL_ID_RECETTE)));
                recette.setTitre(cursor.getString(cursor.getColumnIndex(COL_TITRE)));
                recette.setIngredients(cursor.getString(cursor.getColumnIndex(COL_INGREDIENTS)));
                recette.setEtapes(cursor.getString(cursor.getColumnIndex(COL_ETAPES)));
                recette.setDuree(cursor.getInt(cursor.getColumnIndex(COL_DUREE)));
                recette.setImage(cursor.getString(cursor.getColumnIndex(COL_IMAGE)));
                recette.setTypePlat(cursor.getString(cursor.getColumnIndex(COL_TYPE_PLAT)));
                recettes.add(recette);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recettes;
    }
    
    // --- Méthodes pour les préférences ---
    
    /**
     * Enregistre une préférence
     * @param nom Nom de la préférence
     * @param valeur Valeur de la préférence
     * @return Nombre de lignes affectées ou -1 si erreur
     */
    public long sauvegarderPreference(String nom, String valeur) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_VALEUR_PREFERENCE, valeur);
        
        // Vérifier si la préférence existe déjà
        Cursor cursor = db.query(TABLE_PREFERENCES, 
                new String[]{COL_ID_PREFERENCE}, 
                COL_NOM_PREFERENCE + " = ?", 
                new String[]{nom}, 
                null, null, null);
        
        long result;
        if (cursor.getCount() > 0) {
            // Mettre à jour
            result = db.update(TABLE_PREFERENCES, values, 
                    COL_NOM_PREFERENCE + " = ?", new String[]{nom});
        } else {
            // Insérer nouvelle préférence
            values.put(COL_NOM_PREFERENCE, nom);
            result = db.insert(TABLE_PREFERENCES, null, values);
        }
        cursor.close();
        return result;
    }
    
    /**
     * Récupère la valeur d'une préférence
     * @param nom Nom de la préférence
     * @param defaut Valeur par défaut si la préférence n'existe pas
     * @return Valeur de la préférence
     */
    public String getPreference(String nom, String defaut) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_PREFERENCES, 
                new String[]{COL_VALEUR_PREFERENCE}, 
                COL_NOM_PREFERENCE + " = ?", 
                new String[]{nom}, 
                null, null, null);
        
        String valeur = defaut;
        if (cursor.moveToFirst()) {
            valeur = cursor.getString(cursor.getColumnIndex(COL_VALEUR_PREFERENCE));
        }
        cursor.close();
        return valeur;
    }
}