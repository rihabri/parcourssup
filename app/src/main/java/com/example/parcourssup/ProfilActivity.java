package com.example.parcourssup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class ProfilActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvNomProfil, tvEmailProfil, tvVilleProfil, tvNiveauProfil;
    private MaterialCardView cardMesEcoles, cardDeconnexion;
    private LinearLayout navHome, navNotifications, navServices, navProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ivBack             = findViewById(R.id.ivBack);
        tvNomProfil        = findViewById(R.id.tvNomProfil);
        tvEmailProfil      = findViewById(R.id.tvEmailProfil);
        tvVilleProfil      = findViewById(R.id.tvVilleProfil);
        tvNiveauProfil     = findViewById(R.id.tvNiveauProfil);

        cardMesEcoles      = findViewById(R.id.cardMesEcoles);
        cardDeconnexion    = findViewById(R.id.cardDeconnexion);

        navHome            = findViewById(R.id.nav_home);
        navNotifications   = findViewById(R.id.nav_notifications);
        navServices        = findViewById(R.id.nav_services);
        navProfil          = findViewById(R.id.nav_profil);

        SharedPreferences prefs = getSharedPreferences("ParcoursSup", MODE_PRIVATE);
        String prenom = prefs.getString("prenom", "");
        String nom    = prefs.getString("nom", "");
        String email  = prefs.getString("email", "");
        String ville  = prefs.getString("ville", "");
        String niveau = prefs.getString("niveau", "");

        tvNomProfil.setText(prenom + " " + nom);
        tvEmailProfil.setText(email);
        tvVilleProfil.setText(ville.isEmpty() ? "Non spécifié" : ville);
        tvNiveauProfil.setText(niveau.isEmpty() ? "Non spécifié" : niveau);

        ivBack.setOnClickListener(v -> finish());

        if (cardMesEcoles != null) {
            cardMesEcoles.setOnClickListener(v ->
                    startActivity(new Intent(ProfilActivity.this, MesEcolesActivity.class)));
        }

        if (cardDeconnexion != null) {
            cardDeconnexion.setOnClickListener(v -> {
                prefs.edit().clear().apply();
                Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }


        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                startActivity(new Intent(ProfilActivity.this, HomeActivity.class));
                finish();
            });
        }

        if (navNotifications != null) {
            navNotifications.setOnClickListener(v ->
                    startActivity(new Intent(ProfilActivity.this, SuivreNotificationsActivity.class)));
        }

        if (navServices != null) {
            navServices.setOnClickListener(v -> {
                Intent intent = new Intent(ProfilActivity.this, Step3Activity.class);
                intent.putExtra("fromHome", true);
                startActivity(intent);
            });
        }

        if (navProfil != null) {
            navProfil.setOnClickListener(v ->
                    Toast.makeText(this, "Vous êtes sur votre profil", Toast.LENGTH_SHORT).show());
        }
    }
}