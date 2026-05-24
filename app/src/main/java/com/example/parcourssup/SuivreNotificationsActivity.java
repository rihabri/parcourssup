package com.example.parcourssup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SuivreNotificationsActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private LinearLayout layoutEmpty, layoutPagination;
    private TextView tvPrecedent, tvSuivant, tvPageInfo, tvToutesLues;
    private TextView tvFiltreNonLu, tvFiltreFermeture;
    private ImageView ivBack;

    private NotificationAdapter adapter;
    private List<NotificationItem> allNotifications;
    private List<NotificationItem> filteredNotifications;
    private int currentPage = 0;
    private int itemsPerPage = 5;
    private String currentFilter = "all"; // all, nonLu, fermeture


    private LinearLayout navHome, navNotifications, navServices, navProfile;
    private ImageView ivHome, ivNotifications, ivProfile;
    private TextView tvHome, tvNotifications, tvServices, tvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suivre_notifications);

        initViews();
        setupListeners();
        loadNotifications();
        setupBottomNavigation();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        rvNotifications = findViewById(R.id.rvNotifications);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        layoutPagination = findViewById(R.id.layoutPagination);
        tvPrecedent = findViewById(R.id.tvPrecedent);
        tvSuivant = findViewById(R.id.tvSuivant);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        tvToutesLues = findViewById(R.id.tvToutesLues);
        tvFiltreNonLu = findViewById(R.id.tvFiltreNonLu);
        tvFiltreFermeture = findViewById(R.id.tvFiltreFermeture);

        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        tvToutesLues.setOnClickListener(v -> marquerToutesLues());

        tvFiltreNonLu.setOnClickListener(v -> {
            currentFilter = "nonLu";
            applyFilter();
        });

        tvFiltreFermeture.setOnClickListener(v -> {
            currentFilter = "fermeture";
            applyFilter();
        });

        tvPrecedent.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage();
            }
        });

        tvSuivant.setOnClickListener(v -> {
            int maxPage = (int) Math.ceil((double) filteredNotifications.size() / itemsPerPage) - 1;
            if (currentPage < maxPage) {
                currentPage++;
                displayPage();
            }
        });
    }

    private void loadNotifications() {
        allNotifications = new ArrayList<>();

        allNotifications.add(new NotificationItem("Concours ENSA ouvert", "Les inscriptions au concours ENSA 2026 sont ouvertes jusqu'au 30 juin", "Il y a 2 heures", false, "concours"));
        allNotifications.add(new NotificationItem("Concours ENCG", "Date limite des inscriptions : 15 juillet 2026", "Il y a 1 jour", true, "concours"));
        allNotifications.add(new NotificationItem("Concours UM6P", "Les épreuves auront lieu en septembre 2026", "Il y a 3 jours", false, "concours"));

        allNotifications.add(new NotificationItem("Inscription ESBAC", "Votre dossier a été reçu avec succès", "Il y a 5 heures", false, "inscription"));
        allNotifications.add(new NotificationItem("Inscription ENA", "Complétez votre dossier avant le 30 mai", "Il y a 2 jours", true, "inscription"));

        allNotifications.add(new NotificationItem("Nouvelle école partenaire", "ENSAD Mohammedia rejoint notre plateforme", "Il y a 4 heures", false, "actualite"));
        allNotifications.add(new NotificationItem("Bourse d'étude", "Bourse Fondation Jadara - Date limite 29 avril", "Il y a 6 jours", true, "actualite"));

        allNotifications.add(new NotificationItem(
                "🎓 Nouvelle école disponible",
                "L'École Polytechnique Féminine rejoint ParcoursSup",
                "À l'instant",
                false,
                "ecole"
        ));

        allNotifications.add(new NotificationItem(
                "🏫 Nouveau campus ouvert",
                "EMINES - Université Mohammed VI ouvre ses inscriptions",
                "Il y a 30 minutes",
                false,
                "ecole"
        ));

        allNotifications.add(new NotificationItem("Rappel concours", "N'oubliez pas de préparer votre dossier pour le concours ENSAM", "Il y a 1 heure", false, "rappel"));
        allNotifications.add(new NotificationItem("Date limite approche", "Fermeture des inscriptions ESBAC dans 3 jours", "Il y a 12 heures", false, "rappel"));

        filteredNotifications = new ArrayList<>(allNotifications);
        applyFilter();
    }

    private void applyFilter() {
        filteredNotifications.clear();

        switch (currentFilter) {
            case "nonLu":
                for (NotificationItem notif : allNotifications) {
                    if (!notif.isLu()) {
                        filteredNotifications.add(notif);
                    }
                }
                break;
            case "fermeture":
                for (NotificationItem notif : allNotifications) {
                    if (notif.getType().equals("rappel") || notif.getTitre().contains("limite") || notif.getMessage().contains("Date limite")) {
                        filteredNotifications.add(notif);
                    }
                }
                break;
            default:
                filteredNotifications.addAll(allNotifications);
                break;
        }

        currentPage = 0;
        displayPage();

        updateFilterAppearance();
    }

    private void updateFilterAppearance() {
        int activeColor = getColor(R.color.primary);
        int inactiveColor = getColor(R.color.gray);

        if (currentFilter.equals("nonLu")) {
            tvFiltreNonLu.setTextColor(activeColor);
            tvFiltreFermeture.setTextColor(inactiveColor);
        } else if (currentFilter.equals("fermeture")) {
            tvFiltreNonLu.setTextColor(inactiveColor);
            tvFiltreFermeture.setTextColor(activeColor);
        } else {
            tvFiltreNonLu.setTextColor(inactiveColor);
            tvFiltreFermeture.setTextColor(inactiveColor);
        }
    }

    private void displayPage() {
        int start = currentPage * itemsPerPage;
        int end = Math.min(start + itemsPerPage, filteredNotifications.size());
        List<NotificationItem> pageItems = filteredNotifications.subList(start, end);

        adapter = new NotificationAdapter(pageItems, notification -> {
            Toast.makeText(this, notification.getTitre(), Toast.LENGTH_SHORT).show();
            notification.setLu(true);
            adapter.notifyDataSetChanged();
        });

        rvNotifications.setAdapter(adapter);

        if (filteredNotifications.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            layoutPagination.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            layoutPagination.setVisibility(View.VISIBLE);
        }

        int totalPages = (int) Math.ceil((double) filteredNotifications.size() / itemsPerPage);
        tvPageInfo.setText("Page " + (currentPage + 1) + " / " + Math.max(1, totalPages));
        tvPrecedent.setEnabled(currentPage > 0);
        tvSuivant.setEnabled(currentPage < totalPages - 1);
    }

    private void marquerToutesLues() {
        for (NotificationItem notif : filteredNotifications) {
            notif.setLu(true);
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Toutes les notifications ont été marquées comme lues", Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNavigation() {
        navHome = findViewById(R.id.nav_home);
        navNotifications = findViewById(R.id.nav_notifications);
        navServices = findViewById(R.id.nav_services);
        navProfile = findViewById(R.id.nav_profile);

        ivHome = findViewById(R.id.ivHome);
        ivNotifications = findViewById(R.id.ivNotifications);
        ivProfile = findViewById(R.id.ivProfile);

        tvHome = findViewById(R.id.tvHome);
        tvNotifications = findViewById(R.id.tvNotifications);
        tvServices = findViewById(R.id.tvServices);
        tvProfile = findViewById(R.id.tvProfile);

        navHome.setOnClickListener(v -> {
            finish();
        });

        navNotifications.setOnClickListener(v -> {
        });

        navServices.setOnClickListener(v -> {
            Toast.makeText(this, "Services", Toast.LENGTH_SHORT).show();
        });

        navProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Profil", Toast.LENGTH_SHORT).show();
        });
    }
}