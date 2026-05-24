package com.example.parcourssup;

public class Order {

    private long   id;
    private long   userId;
    private String pack;
    private String prix;
    private String statut;
    private String dateCommande;


    private String userPrenom;
    private String userNom;
    private String userTel;


    public Order() {}

    public Order(long userId, String pack, String prix) {
        this.userId = userId;
        this.pack   = pack;
        this.prix   = prix;
        this.statut = "En attente";
    }


    public String getUserFullName() {
        if (userPrenom != null && userNom != null)
            return userPrenom + " " + userNom;
        return "Utilisateur #" + userId;
    }


    public String getSummary() {
        return "📦 " + pack + "  •  💰 " + prix + "\n"
                + "👤 " + getUserFullName() + "  •  📞 " + (userTel != null ? userTel : "");
    }

    @Override
    public String toString() {
        return pack + " — " + getUserFullName();
    }


    public long getId()                     { return id; }
    public void setId(long id)              { this.id = id; }

    public long getUserId()                 { return userId; }
    public void setUserId(long userId)      { this.userId = userId; }

    public String getPack()                 { return pack; }
    public void setPack(String pack)        { this.pack = pack; }

    public String getPrix()                 { return prix; }
    public void setPrix(String prix)        { this.prix = prix; }

    public String getStatut()               { return statut; }
    public void setStatut(String statut)    { this.statut = statut; }

    public String getDateCommande()             { return dateCommande; }
    public void setDateCommande(String date)    { this.dateCommande = date; }

    public String getUserPrenom()               { return userPrenom; }
    public void setUserPrenom(String userPrenom){ this.userPrenom = userPrenom; }

    public String getUserNom()                  { return userNom; }
    public void setUserNom(String userNom)      { this.userNom = userNom; }

    public String getUserTel()                  { return userTel; }
    public void setUserTel(String userTel)      { this.userTel = userTel; }
}
