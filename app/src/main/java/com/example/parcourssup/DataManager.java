package com.example.parcourssup;

public class DataManager {
    private static DataManager instance;

    private String prenom, nom, age, telephone, ville;
    private String email, password, numeroScolaire;
    private String niveau, annee, filiere, note, bacLibre;

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void setStep1Data(String prenom, String nom, String age, String telephone, String ville) {
        this.prenom = prenom;
        this.nom = nom;
        this.age = age;
        this.telephone = telephone;
        this.ville = ville;
    }

    public String getPrenom() { return prenom; }
    public String getNom() { return nom; }
    public String getAge() { return age; }
    public String getTelephone() { return telephone; }
    public String getVille() { return ville; }

    public void setStep2Data(String email, String password, String numeroScolaire,
                             String niveau, String annee, String filiere, String note, String bacLibre) {
        this.email = email;
        this.password = password;
        this.numeroScolaire = numeroScolaire;
        this.niveau = niveau;
        this.annee = annee;
        this.filiere = filiere;
        this.note = note;
        this.bacLibre = bacLibre;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNumeroScolaire() { return numeroScolaire; }
    public String getNiveau() { return niveau; }
    public String getAnnee() { return annee; }
    public String getFiliere() { return filiere; }
    public String getNote() { return note; }
    public String getBacLibre() { return bacLibre; }

    public void clearData() {
        prenom = null;
        nom = null;
        age = null;
        telephone = null;
        ville = null;
        email = null;
        password = null;
        numeroScolaire = null;
        niveau = null;
        annee = null;
        filiere = null;
        note = null;
        bacLibre = null;
    }
}