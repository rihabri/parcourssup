package com.example.parcourssup;

import android.content.Context;
import android.content.SharedPreferences;

public class DataStorage {
    private static final String PREF_NAME = "inscription_data";
    private static DataStorage instance;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private DataStorage(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized DataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new DataStorage(context);
        }
        return instance;
    }

    public void saveStep1(String prenom, String nom, String age, String telephone, String ville) {
        editor.putString("prenom", prenom);
        editor.putString("nom", nom);
        editor.putString("age", age);
        editor.putString("telephone", telephone);
        editor.putString("ville", ville);
        editor.putInt("step", 1);
        editor.apply();
    }

    public void saveStep2(String email, String password, String numeroScolaire,
                          String niveau, String annee, String filiere, String note, String bacLibre) {
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("numeroScolaire", numeroScolaire);
        editor.putString("niveau", niveau);
        editor.putString("annee", annee);
        editor.putString("filiere", filiere);
        editor.putString("note", note);
        editor.putString("bacLibre", bacLibre);
        editor.putInt("step", 2);
        editor.apply();
    }

    public String getPrenom() { return prefs.getString("prenom", ""); }
    public String getNom() { return prefs.getString("nom", ""); }
    public String getAge() { return prefs.getString("age", ""); }
    public String getTelephone() { return prefs.getString("telephone", ""); }
    public String getVille() { return prefs.getString("ville", ""); }
    public String getEmail() { return prefs.getString("email", ""); }
    public String getPassword() { return prefs.getString("password", ""); }
    public String getNumeroScolaire() { return prefs.getString("numeroScolaire", ""); }
    public String getNiveau() { return prefs.getString("niveau", ""); }
    public String getAnnee() { return prefs.getString("annee", ""); }
    public String getFiliere() { return prefs.getString("filiere", ""); }
    public String getNote() { return prefs.getString("note", ""); }
    public String getBacLibre() { return prefs.getString("bacLibre", ""); }
    public int getStep() { return prefs.getInt("step", 0); }

    public boolean hasStep1Data() {
        return !getPrenom().isEmpty() && !getNom().isEmpty();
    }

    public boolean hasStep2Data() {
        return !getEmail().isEmpty() && !getPassword().isEmpty();
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}