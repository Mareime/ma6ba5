package com.example.ma6ba5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class PreferencesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RadioGroup rgTheme, rgTailleTexte;
    private Button btnEnregistrer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        
        // Initialiser la base de données
        dbHelper = new DatabaseHelper(this);
        
        // Initialiser les vues
        rgTheme = findViewById(R.id.rg_theme);
        rgTailleTexte = findViewById(R.id.rg_taille_texte);
        btnEnregistrer = findViewById(R.id.btn_enregistrer);
        
        // Charger les préférences actuelles
        chargerPreferences();
        
        // Configurer le bouton d'enregistrement
        btnEnregistrer.setOnClickListener(v -> enregistrerPreferences());
    }
    
    private void chargerPreferences() {
        // Charger la préférence du thème
        String theme = dbHelper.getPreference("theme", "clair");
        if (theme.equals("clair")) {
            ((RadioButton) rgTheme.findViewById(R.id.rb_theme_clair)).setChecked(true);
        } else {
            ((RadioButton) rgTheme.findViewById(R.id.rb_theme_sombre)).setChecked(true);
        }
        
        // Charger la préférence de taille de texte
        String tailleTexte = dbHelper.getPreference("taille_texte", "normale");
        switch (tailleTexte) {
            case "petite":
                ((RadioButton) rgTailleTexte.findViewById(R.id.rb_taille_petite)).setChecked(true);
                break;
            case "grande":
                ((RadioButton) rgTailleTexte.findViewById(R.id.rb_taille_grande)).setChecked(true);
                break;
            default:
                ((RadioButton) rgTailleTexte.findViewById(R.id.rb_taille_normale)).setChecked(true);
                break;
        }
    }
    
    private void enregistrerPreferences() {
        // Récupérer le thème sélectionné
        int themeId = rgTheme.getCheckedRadioButtonId();
        String theme = "clair";
        if (themeId == R.id.rb_theme_sombre) {
            theme = "sombre";
        }
        
        // Récupérer la taille de texte sélectionnée
        int tailleTexteId = rgTailleTexte.getCheckedRadioButtonId();
        String tailleTexte = "normale";
        if (tailleTexteId == R.id.rb_taille_petite) {
            tailleTexte = "petite";
        } else if (tailleTexteId == R.id.rb_taille_grande) {
            tailleTexte = "grande";
        }
        
        // Sauvegarder les préférences
        dbHelper.sauvegarderPreference("theme", theme);
        dbHelper.sauvegarderPreference("taille_texte", tailleTexte);
        
        // Appliquer le thème
        appliquerTheme(theme);
        
        // Afficher un message
        Toast.makeText(this, R.string.preferences_enregistrees, Toast.LENGTH_SHORT).show();
        
        // Fermer l'activité
        finish();
    }
    
    private void appliquerTheme(String theme) {
        if (theme.equals("sombre")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}