package com.example.parcourssup;

public class InscriptionEcole {
    private String nom;
    private String diplome;
    private String ville;
    private String icone;
    private String date;

    public InscriptionEcole(String nom, String diplome, String ville, String icone, String date) {
        this.nom = nom;
        this.diplome = diplome;
        this.ville = ville;
        this.icone = icone;
        this.date = date;
    }

    public String getNom() { return nom; }
    public String getDiplome() { return diplome; }
    public String getVille() { return ville; }
    public String getIcone() { return icone; }
    public String getDate() { return date; }
}
