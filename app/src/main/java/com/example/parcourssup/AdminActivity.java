package com.example.parcourssup;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView rvList;
    private FloatingActionButton fabAdd;
    private TextView tvStatUsers, tvStatOrders, tvStatEcoles;

    private DBHelper db;

    private List<User>  userList  = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private List<Ecole> ecoleList = new ArrayList<>();

    private SimpleAdapter<User>  userAdapter;
    private SimpleAdapter<Order> orderAdapter;
    private SimpleAdapter<Ecole> ecoleAdapter;

    private int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = DBHelper.getInstance(this);

        initViews();
        setupTabs();
        setupFab();
        refreshStats();
        loadTab(0);
    }

    private void initViews() {
        tabLayout    = findViewById(R.id.tabLayout);
        rvList       = findViewById(R.id.rvList);
        fabAdd       = findViewById(R.id.fabAdd);
        tvStatUsers  = findViewById(R.id.tvStatUsers);
        tvStatOrders = findViewById(R.id.tvStatOrders);
        tvStatEcoles = findViewById(R.id.tvStatEcoles);

        rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("👤 Utilisateurs"));
        tabLayout.addTab(tabLayout.newTab().setText("📦 Commandes"));
        tabLayout.addTab(tabLayout.newTab().setText("🏫 Écoles"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                loadTab(currentTab);
                fabAdd.setVisibility(currentTab == 1 ? View.GONE : View.VISIBLE);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupFab() {
        fabAdd.setOnClickListener(v -> {
            if (currentTab == 2) showAddEcoleDialog(null);
        });
    }

    private void loadTab(int tab) {
        switch (tab) {
            case 0: loadUsers();  break;
            case 1: loadOrders(); break;
            case 2: loadEcoles(); break;
        }
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(db.getAllUsers());

        userAdapter = new SimpleAdapter<>(this, userList);

        userAdapter.setOnDeleteClickListener((user, position) ->
                confirmDeleteUser(user, position));

        userAdapter.setOnItemClickListener((user, position) ->
                showUserDetail(user));

        rvList.setAdapter(userAdapter);
    }

    private void loadOrders() {
        orderList.clear();
        orderList.addAll(db.getAllOrders());

        orderAdapter = new SimpleAdapter<>(this, orderList);

        orderAdapter.setOnDeleteClickListener((order, position) ->
                confirmDeleteOrder(order, position));

        orderAdapter.setOnItemClickListener((order, position) ->
                showOrderDetail(order));

        rvList.setAdapter(orderAdapter);
    }

    private void loadEcoles() {
        ecoleList.clear();
        ecoleList.addAll(db.getAllEcoles());

        ecoleAdapter = new SimpleAdapter<>(this, ecoleList);

        ecoleAdapter.setOnDeleteClickListener((ecole, position) ->
                confirmDeleteEcole(ecole, position));

        ecoleAdapter.setOnItemClickListener((ecole, position) ->
                showAddEcoleDialog(ecole));

        rvList.setAdapter(ecoleAdapter);
    }

    private void refreshStats() {
        tvStatUsers.setText(String.valueOf(db.getUserCount()));
        tvStatOrders.setText(String.valueOf(db.getOrderCount()));
        tvStatEcoles.setText(String.valueOf(db.getEcoleCount()));
    }

    private void showUserDetail(User user) {
        StringBuilder detail = new StringBuilder();
        detail.append("👤 Nom complet : ").append(user.getFullName()).append("\n")
                .append("🔢 Âge : ").append(nullSafe(user.getAge())).append("\n")
                .append("📞 Téléphone : ").append(nullSafe(user.getTelephone())).append("\n")
                .append("🏙 Ville : ").append(nullSafe(user.getVille())).append("\n")
                .append("📧 Email : ").append(nullSafe(user.getEmail())).append("\n")
                .append("📦 Pack : ").append(nullSafe(user.getPack())).append("\n")
                .append("💳 Paiement : ").append(nullSafe(user.getStatutPaiement()));

        detail.append("\n\n─────────────────────\n");
        detail.append("📚 Écoles choisies :\n");

        List<Ecole> ecoles = db.getUserEcoles(user.getId());
        if (ecoles.isEmpty()) {
            detail.append("  📭 Aucune école choisie pour le moment");
        } else {
            for (Ecole e : ecoles) {
                detail.append("  🏫 ").append(e.getNom())
                        .append(" — ").append(nullSafe(e.getVille())).append("\n");
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Détails Utilisateur")
                .setMessage(detail.toString())
                .setPositiveButton("Fermer", null)
                .setNegativeButton("Supprimer", (d, w) ->
                        confirmDeleteUser(user, userList.indexOf(user)))
                .show();
    }

    private void showOrderDetail(Order order) {

        String telephone = nullSafe(order.getUserTel());
        if (telephone.equals("—")) {
            User u = db.getUserById(order.getUserId());
            if (u != null) telephone = nullSafe(u.getTelephone());
        }

        final String telFinal = telephone;

        // ── Récupérer le statut paiement actuel de l'utilisateur ──
        String statutPaiement = db.getStatutPaiement(order.getUserId());

        String detail =
                "📦 Pack : "        + nullSafe(order.getPack())     + "\n" +
                        "💰 Prix : "        + nullSafe(order.getPrix())     + "\n" +
                        "✅ Statut : "      + nullSafe(order.getStatut())   + "\n" +
                        "👤 Client : "      + order.getUserFullName()       + "\n" +
                        "📞 Téléphone : "   + telFinal                      + "\n" +
                        "💳 Paiement : "    + statutPaiement;

        String[] statuts = {"En attente", "Confirmée", "Annulée", "Traitée"};

        new AlertDialog.Builder(this)
                .setTitle("Détails Commande")
                .setMessage(detail)
                .setPositiveButton("Fermer", null)
                .setNeutralButton("✏️ Changer statut", (d, w) -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Choisir le statut")
                            .setItems(statuts, (d2, which) -> {
                                String nouveauStatut = statuts[which];

                                // ── Mettre à jour le statut de la commande ──
                                db.updateOrderStatut(order.getId(), nouveauStatut);
                                order.setStatut(nouveauStatut);

                                // ── Si confirmée → marquer paiement comme "payé" dans users ──
                                if (nouveauStatut.equals("Confirmée")) {
                                    db.updateStatutPaiement(order.getUserId(), "payé");
                                    Toast.makeText(this,
                                            "✅ Paiement confirmé ! L'utilisateur peut s'inscrire.",
                                            Toast.LENGTH_LONG).show();
                                }

                                // ── Si annulée → remettre "en_attente" ──
                                else if (nouveauStatut.equals("Annulée")) {
                                    db.updateStatutPaiement(order.getUserId(), "en_attente");
                                    Toast.makeText(this,
                                            "❌ Paiement annulé.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ── Autres statuts ──
                                else {
                                    Toast.makeText(this,
                                            "Statut mis à jour : " + nouveauStatut,
                                            Toast.LENGTH_SHORT).show();
                                }

                                loadOrders();
                            })
                            .setNegativeButton("Annuler", null)
                            .show();
                })
                .show();
    }

    private void confirmDeleteOrder(Order order, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer la commande ?")
                .setMessage("Supprimer la commande de " + order.getUserFullName() +
                        " (Pack : " + order.getPack() + ") ?")
                .setPositiveButton("Supprimer", (d, w) -> {
                    if (db.deleteOrder(order.getId())) {
                        if (position >= 0 && position < orderList.size()) {
                            orderAdapter.removeItem(position);
                        }
                        refreshStats();
                        Toast.makeText(this,
                                "Commande supprimée", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void confirmDeleteUser(User user, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'utilisateur ?")
                .setMessage("Supprimer " + user.getFullName() +
                        " et toutes ses commandes ?")
                .setPositiveButton("Supprimer", (d, w) -> {
                    if (db.deleteUser(user.getId())) {
                        if (position >= 0 && position < userList.size()) {
                            userAdapter.removeItem(position);
                        }
                        refreshStats();
                        Toast.makeText(this,
                                "Utilisateur supprimé", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void confirmDeleteEcole(Ecole ecole, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'école ?")
                .setMessage("Supprimer " + ecole.getNom() + " ?")
                .setPositiveButton("Supprimer", (d, w) -> {
                    if (db.deleteEcole(ecole.getId())) {
                        if (position >= 0 && position < ecoleList.size()) {
                            ecoleAdapter.removeItem(position);
                        }
                        refreshStats();
                        Toast.makeText(this,
                                "École supprimée", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showAddEcoleDialog(Ecole ecoleToEdit) {

        boolean isEdit = (ecoleToEdit != null);

        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_ecole, null);

        EditText etNom     = dialogView.findViewById(R.id.etEcoleNom);
        EditText etNomAr   = dialogView.findViewById(R.id.etEcoleNomArab);
        EditText etType    = dialogView.findViewById(R.id.etEcoleType);
        EditText etVille   = dialogView.findViewById(R.id.etEcoleVille);
        EditText etPrix    = dialogView.findViewById(R.id.etEcolePrix);
        EditText etDuree   = dialogView.findViewById(R.id.etEcoleDuree);
        EditText etDiplome = dialogView.findViewById(R.id.etEcoleDiplome);
        EditText etFiliere = dialogView.findViewById(R.id.etEcoleFiliere);

        if (isEdit) {
            etNom.setText(ecoleToEdit.getNom());
            etNomAr.setText(ecoleToEdit.getNomArab());
            etType.setText(ecoleToEdit.getType());
            etVille.setText(ecoleToEdit.getVille());
            etPrix.setText(ecoleToEdit.getPrix());
            etDuree.setText(ecoleToEdit.getDuree());
            etDiplome.setText(ecoleToEdit.getDiplome());
            etFiliere.setText(ecoleToEdit.getFiliere());
        }

        new AlertDialog.Builder(this)
                .setTitle(isEdit ? "✏️ Modifier l'école" : "➕ Ajouter une école")
                .setView(dialogView)
                .setPositiveButton(isEdit ? "Modifier" : "Ajouter", (d, w) -> {

                    String nom     = etNom.getText().toString().trim();
                    String nomAr   = etNomAr.getText().toString().trim();
                    String type    = etType.getText().toString().trim();
                    String ville   = etVille.getText().toString().trim();
                    String prix    = etPrix.getText().toString().trim();
                    String duree   = etDuree.getText().toString().trim();
                    String diplome = etDiplome.getText().toString().trim();
                    String filiere = etFiliere.getText().toString().trim();

                    if (nom.isEmpty()) {
                        Toast.makeText(this,
                                "Le nom est obligatoire", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Ecole ecole = new Ecole(nom, nomAr, type, ville,
                            prix, duree, diplome, filiere);

                    if (isEdit) {
                        ecole.setId(ecoleToEdit.getId());
                        db.updateEcole(ecole);
                        Toast.makeText(this,
                                "École mise à jour ✅", Toast.LENGTH_SHORT).show();
                    } else {
                        long newId = db.addEcole(ecole);
                        ecole.setId(newId);
                        Toast.makeText(this,
                                "École ajoutée ✅", Toast.LENGTH_SHORT).show();
                    }

                    refreshStats();
                    loadEcoles();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private String nullSafe(String s) {
        return (s == null || s.isEmpty()) ? "—" : s;
    }
}