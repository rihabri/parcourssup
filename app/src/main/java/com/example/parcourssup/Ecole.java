package com.example.parcourssup;

public class Ecole {


    private long id;

    private String nom;
    private String nomArab;
    private String type;
    private String ville;
    private String prix;
    private String duree;
    private String diplome;
    private String filiere;
    private String dateLimite;



    public Ecole() {
    }

    public Ecole(String nom,
                 String nomArab,
                 String type,
                 String ville,
                 String prix,
                 String duree,
                 String diplome,
                 String filiere) {

        this.nom = nom;
        this.nomArab = nomArab;
        this.type = type;
        this.ville = ville;
        this.prix = prix;
        this.duree = duree;
        this.diplome = diplome;
        this.filiere = filiere;
    }


    public Ecole(String nom,
                 String nomArab,
                 String type,
                 String ville,
                 String prix,
                 String duree,
                 String diplome,
                 String filiere,
                 String dateLimite) {

        this.nom = nom;
        this.nomArab = nomArab;
        this.type = type;
        this.ville = ville;
        this.prix = prix;
        this.duree = duree;
        this.diplome = diplome;
        this.filiere = filiere;
        this.dateLimite = dateLimite;
    }



    public String getSummary() {

        return "🏙 " + ville +
                " • " + type +
                "\n🎓 " + diplome +
                " • 💰 " + prix;
    }

    @Override
    public String toString() {
        return nom;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomArab() {
        return nomArab;
    }

    public void setNomArab(String nomArab) {
        this.nomArab = nomArab;
    }

    public String getNomAr() {
        return nomArab;
    }

    public void setNomAr(String nomArab) {
        this.nomArab = nomArab;
    }

    // ---------- TYPE ----------
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // ---------- VILLE ----------
    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getDiplome() {
        return diplome;
    }

    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(String dateLimite) {
        this.dateLimite = dateLimite;
    }
}