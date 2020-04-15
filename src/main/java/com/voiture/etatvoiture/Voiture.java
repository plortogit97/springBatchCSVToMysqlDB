package com.voiture.etatvoiture;

public class Voiture {

    private int id;
    private String marque;
    private String modele;
    private String etat;

    public Voiture(String marque, String modele, String etat) {
    }

    public String getMarque() {
        return marque;
    }

    public void setNom(String nom) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}