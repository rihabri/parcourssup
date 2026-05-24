package com.example.parcourssup;

public class NotificationItem {
    private String titre;
    private String message;
    private String temps;
    private boolean isLu;
    private String type;

    public NotificationItem(String titre, String message, String temps, boolean isLu, String type) {
        this.titre = titre;
        this.message = message;
        this.temps = temps;
        this.isLu = isLu;
        this.type = type;
    }

    public String getTitre() { return titre; }
    public String getMessage() { return message; }
    public String getTemps() { return temps; }
    public boolean isLu() { return isLu; }
    public void setLu(boolean lu) { isLu = lu; }
    public String getType() { return type; }
}

