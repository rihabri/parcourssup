package com.example.parcourssup;

public class User {

    private long id;
    private String prenom;
    private String nom;
    private String age;
    private String telephone;
    private String ville;
    private String email;
    private String password;
    private String dateInscription;
    private String pack;           // ← AJOUTÉ
    private String statutPaiement; // ← AJOUTÉ

    public User() {
    }

    public User(String prenom, String nom, String age, String telephone, String ville, String email) {
        this.prenom = prenom;
        this.nom = nom;
        this.age = age;
        this.telephone = telephone;
        this.ville = ville;
        this.email = email;
    }

    public String getFullName() {
        return prenom + " " + nom;
    }

    public String getSummary() {
        return "📧 " + email + " • 📞 " + (telephone != null ? telephone : "");
    }

    @Override
    public String toString() {
        return getFullName();
    }

    // ─── Getters & Setters existants ───

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDateInscription() { return dateInscription; }
    public void setDateInscription(String dateInscription) { this.dateInscription = dateInscription; }

    // ─── Nouveaux Getters & Setters ───

    public String getPack() { return pack; }
    public void setPack(String pack) { this.pack = pack; }

    public String getStatutPaiement() { return statutPaiement; }
    public void setStatutPaiement(String statutPaiement) { this.statutPaiement = statutPaiement; }
}