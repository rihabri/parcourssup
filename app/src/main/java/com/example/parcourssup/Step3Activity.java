package com.example.parcourssup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Step3Activity extends AppCompatActivity {

    Button btnAjouter1, btnAjouter2, btnAjouter3, btnFinaliser;

    String prenom, nom, telephone, ville, email;
    long userId;

    String packChoisi = "";
    String prixChoisi = "";

    private boolean whatsappOuvert = false;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);

        db = DBHelper.getInstance(this);

        btnAjouter1  = findViewById(R.id.btnAjouter1);
        btnAjouter2  = findViewById(R.id.btnAjouter2);
        btnAjouter3  = findViewById(R.id.btnAjouter3);
        btnFinaliser = findViewById(R.id.btnFinaliser);

        btnFinaliser.setVisibility(View.GONE);

        prenom    = getIntent().getStringExtra("prenom");
        nom       = getIntent().getStringExtra("nom");
        telephone = getIntent().getStringExtra("telephone");
        ville     = getIntent().getStringExtra("ville");
        email     = getIntent().getStringExtra("email");
        userId    = getIntent().getLongExtra("userId", -1);

        SharedPreferences prefs = getSharedPreferences("ParcoursSup", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("prenom",    prenom);
        editor.putString("nom",       nom);
        editor.putString("telephone", telephone);
        editor.putString("ville",     ville);
        editor.putString("email",     email);
        editor.putLong("userId",      userId);
        editor.apply();

        btnAjouter1.setOnClickListener(v -> selectPack("TAWJIH PLUS",    "400 DH"));
        btnAjouter2.setOnClickListener(v -> selectPack("TASSJIL TOP 15", "1800 DH"));
        btnAjouter3.setOnClickListener(v -> selectPack("TASSJIL PLUS",   "2150 DH"));

        btnFinaliser.setOnClickListener(v -> {

            if (packChoisi.isEmpty()) {
                Toast.makeText(this, "Choisir une offre", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userId != -1) {

                // ── 1. Sauvegarder le pack dans SQLite ──
                db.updateUserPack(userId, packChoisi);

                // ── 2. Statut paiement = "en_attente" par défaut ──
                db.updateStatutPaiement(userId, "en_attente");

                // ── 3. Créer la commande ──
                Order order = new Order();
                order.setUserId(userId);
                order.setPack(packChoisi);
                order.setPrix(prixChoisi);
                order.setStatut("En attente");

                long orderId = db.addOrder(order);

                if (orderId == -1) {
                    Toast.makeText(this,
                            "Erreur lors de l'enregistrement",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

            } else {
                Toast.makeText(this,
                        "Erreur: Utilisateur non identifié",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ouvrirWhatsApp();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (whatsappOuvert) {
            whatsappOuvert = false;
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void ouvrirWhatsApp() {
        String nomComplet = (prenom != null ? prenom : "") + " " + (nom != null ? nom : "");

        String message =
                "Bonjour 👋\n" +
                        "Je viens de m'inscrire sur ParcoursSup.\n\n" +
                        "👤 Nom : " + nomComplet.trim() + "\n" +
                        "📦 Pack : " + packChoisi + "\n" +
                        "💰 Prix : " + prixChoisi + "\n\n" +
                        "Je souhaite procéder au paiement. Merci 🙏";

        String numero = "212699641256";

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse(
                    "https://api.whatsapp.com/send?phone=" + numero +
                            "&text=" + Uri.encode(message)));

            if (intent.resolveActivity(getPackageManager()) != null) {
                whatsappOuvert = true;
                startActivity(intent);
                return;
            }
        } catch (Exception ignored) {}

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.whatsapp.w4b");
            intent.setData(Uri.parse(
                    "https://api.whatsapp.com/send?phone=" + numero +
                            "&text=" + Uri.encode(message)));

            if (intent.resolveActivity(getPackageManager()) != null) {
                whatsappOuvert = true;
                startActivity(intent);
                return;
            }
        } catch (Exception ignored) {}

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/" + numero +
                            "?text=" + Uri.encode(message)));
            whatsappOuvert = true;
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this,
                    "Impossible d'ouvrir WhatsApp", Toast.LENGTH_LONG).show();
        }
    }

    private void selectPack(String pack, String prix) {
        packChoisi = pack;
        prixChoisi = prix;
        btnFinaliser.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Pack choisi : " + pack, Toast.LENGTH_SHORT).show();
    }
}