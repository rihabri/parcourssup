package com.example.parcourssup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MesEcolesActivity extends AppCompatActivity {

    private RecyclerView rvMesEcoles;
    private ImageView ivBack;
    private LinearLayout emptyStateLayout;
    private Button btnExplorer;
    private InscriptionAdapter adapter;
    private List<InscriptionEcole> inscriptionList;

    private DBHelper db;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_ecoles);

        SharedPreferences prefs = getSharedPreferences("ParcoursSup", MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);

        db = DBHelper.getInstance(this);

        rvMesEcoles = findViewById(R.id.rvMesEcoles);
        ivBack = findViewById(R.id.ivBack);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        btnExplorer = findViewById(R.id.btnExplorer);

        inscriptionList = new ArrayList<>();
        adapter = new InscriptionAdapter(inscriptionList, this::supprimerEcole);

        if (rvMesEcoles != null) {
            rvMesEcoles.setLayoutManager(new LinearLayoutManager(this));
            rvMesEcoles.setAdapter(adapter);
        }

        loadMesInscriptions();

        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        if (btnExplorer != null) {
            btnExplorer.setOnClickListener(v -> {
                Intent intent = new Intent(MesEcolesActivity.this, DecouverteEcolesActivity.class);
                startActivity(intent);
            });
        }
    }

    private void loadMesInscriptions() {
        if (userId == -1) return;

          List<Ecole> ecoles = db.getUserEcoles(userId);
        inscriptionList.clear();

        if (ecoles.isEmpty()) {
            if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.VISIBLE);
            if (rvMesEcoles != null) rvMesEcoles.setVisibility(View.GONE);
        } else {
            if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.GONE);
            if (rvMesEcoles != null) rvMesEcoles.setVisibility(View.VISIBLE);

            for (Ecole ecole : ecoles) {
                InscriptionEcole inscription = new InscriptionEcole(
                        ecole.getNom(),
                        ecole.getDiplome(),
                        ecole.getVille(),
                        "🏫",
                        getCurrentDate()
                );
                inscriptionList.add(inscription);
            }

            if (adapter != null) adapter.notifyDataSetChanged();
        }
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "dd/MM/yyyy", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }

    private void supprimerEcole(String ecoleNom) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Voulez-vous vraiment supprimer " + ecoleNom + " de vos écoles ?")
                .setPositiveButton("Oui", (dialog, which) -> {

                    List<Ecole> toutesEcoles = db.getAllEcoles();
                    for (Ecole e : toutesEcoles) {
                        if (e.getNom().equals(ecoleNom)) {
                            db.deleteUserEcole(userId, e.getId());
                            break;
                        }
                    }

                    loadMesInscriptions();
                    Toast.makeText(this, ecoleNom + " a été supprimée", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMesInscriptions();
    }
}