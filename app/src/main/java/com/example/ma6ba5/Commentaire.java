package com.example.ma6ba5;

public class Commentaire {
    private Long id;
    private String contenu;
    private CibleType typeCible;
    private Long idCible;

    public enum CibleType {
        RECETTE,
        BOISSON
    }

    public Commentaire() {
    }

    public Commentaire(String contenu, CibleType typeCible, Long idCible) {
        this.contenu = contenu;
        this.typeCible = typeCible;
        this.idCible = idCible;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public CibleType getTypeCible() {
        return typeCible;
    }

    public void setTypeCible(CibleType typeCible) {
        this.typeCible = typeCible;
    }

    public Long getIdCible() {
        return idCible;
    }

    public void setIdCible(Long idCible) {
        this.idCible = idCible;
    }

    // Utility methods
    public boolean isForRecette() {
        return this.typeCible == CibleType.RECETTE;
    }

    public boolean isForBoisson() {
        return this.typeCible == CibleType.BOISSON;
    }
}