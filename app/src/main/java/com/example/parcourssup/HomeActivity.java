package com.example.parcourssup;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {

    private TextView tvBonjour;
    private EditText etRecherche;
    private Button   btnAdmin;

    private MaterialCardView cardDecouverteEcoles, cardSuivreNotifications, cardMesEcoles;
    private MaterialCardView cardUm6p, cardJadara, cardMilitaire;

    private LinearLayout navHome, navNotifications, navServices, navProfil;

    private static final String ADMIN_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("ParcoursSup", MODE_PRIVATE);
        String prenomUser = prefs.getString("prenom", "");

        initViews();

        if (prenomUser != null && !prenomUser.isEmpty()) {
            tvBonjour.setText(getString(R.string.bonjour_user, prenomUser));
        } else {
            tvBonjour.setText(getString(R.string.bonjour));
        }

        setupAdminAccess();
        setupCards();
        setupBottomNav();
        setupSearch();
    }

    private void initViews() {
        tvBonjour               = findViewById(R.id.tvBonjour);
        etRecherche             = findViewById(R.id.etRecherche);
        btnAdmin                = findViewById(R.id.btnAdmin);

        cardDecouverteEcoles    = findViewById(R.id.cardDecouverteEcoles);
        cardSuivreNotifications = findViewById(R.id.cardSuivreNotifications);
        cardMesEcoles           = findViewById(R.id.cardMesEcoles);

        cardUm6p                = findViewById(R.id.cardUm6p);
        cardJadara              = findViewById(R.id.cardJadara);
        cardMilitaire           = findViewById(R.id.cardMilitaire);

        navHome                 = findViewById(R.id.nav_home);
        navNotifications        = findViewById(R.id.nav_notifications);
        navServices             = findViewById(R.id.nav_services);
        navProfil               = findViewById(R.id.nav_profil);
    }


    private void setupAdminAccess() {
        if (btnAdmin != null) {
            btnAdmin.setOnClickListener(v -> showAdminPasswordDialog());
        }

        tvBonjour.setOnLongClickListener(v -> {
            showAdminPasswordDialog();
            return true;
        });
    }

    private void showAdminPasswordDialog() {
        EditText etPassword = new EditText(this);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setHint(R.string.admin_password_hint);

        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        etPassword.setPadding(padding, padding, padding, padding);

        new AlertDialog.Builder(this)
                .setTitle(R.string.admin_access_title)
                .setMessage(R.string.admin_access_message)
                .setView(etPassword)
                .setPositiveButton(R.string.login, (dialog, which) -> {
                    String entered = etPassword.getText().toString().trim();
                    if (entered.equals(ADMIN_PASSWORD)) {
                        startActivity(new Intent(HomeActivity.this, AdminActivity.class));
                    } else {
                        Toast.makeText(this,
                                R.string.admin_password_incorrect, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void setupCards() {
        cardDecouverteEcoles.setOnClickListener(v ->
                startActivity(new Intent(this, DecouverteEcolesActivity.class)));

        cardSuivreNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, SuivreNotificationsActivity.class)));

        cardMesEcoles.setOnClickListener(v ->
                startActivity(new Intent(this, MesEcolesActivity.class)));


    }

    // ================= NAVIGATION =================
    private void setupBottomNav() {
        navHome.setOnClickListener(v ->
                Toast.makeText(this, R.string.home, Toast.LENGTH_SHORT).show());

        navNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, SuivreNotificationsActivity.class)));

        navServices.setOnClickListener(v ->
                startActivity(new Intent(this, ServicesActivity.class)));

        navProfil.setOnClickListener(v ->
                startActivity(new Intent(this, ProfilActivity.class)));
    }

    // ================= SEARCH =================
    private void setupSearch() {
        etRecherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    Toast.makeText(HomeActivity.this,
                            getString(R.string.search_message, s), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}