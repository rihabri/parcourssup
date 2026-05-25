package com.example.parcourssup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DecouverteEcolesActivity extends AppCompatActivity {

    private RecyclerView rvEcoles;
    private ImageView ivBack;
    private EcoleAdapter adapter;
    private List<Ecole> ecoleList;

    private static final String TAG = "DecouverteEcoles";

    private DBHelper db;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decouverte_ecoles);

        SharedPreferences prefs = getSharedPreferences("ParcoursSup", MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);

        db = DBHelper.getInstance(this);

        rvEcoles = findViewById(R.id.rvEcoles);
        ivBack = findViewById(R.id.ivBack);

        ecoleList = new ArrayList<>();
        adapter = new EcoleAdapter(ecoleList, this, this::ajouterAuxInscriptions);

        rvEcoles.setLayoutManager(new LinearLayoutManager(this));
        rvEcoles.setAdapter(adapter);

        loadEcolesData();

        ivBack.setOnClickListener(v -> finish());
    }

    private void ajouterAuxInscriptions(Ecole ecole) {

        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        String statut = db.getStatutPaiement(userId);
        if (!statut.equals("payé")) {
            new AlertDialog.Builder(this)
                    .setTitle("⚠️ Paiement requis")
                    .setMessage("Vous devez confirmer votre paiement avant de vous inscrire à une école.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        String pack = db.getUserPack(userId);
        int limite = getLimiteEcoles(pack);
        int nbEcoles = db.getUserEcoleCount(userId);

        if (nbEcoles >= limite) {
            new AlertDialog.Builder(this)
                    .setTitle("🚫 Limite atteinte")
                    .setMessage("Votre pack \"" + pack + "\" permet " + limite +
                            " écoles maximum.\nVous avez déjà " + nbEcoles + " école(s).")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        Ecole ecoleDB = getOrCreateEcole(ecole);
        if (ecoleDB == null) {
            Toast.makeText(this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.isUserEcoleExists(userId, ecoleDB.getId())) {
            Toast.makeText(this, "Vous êtes déjà inscrit à " + ecole.getNom(), Toast.LENGTH_SHORT).show();
            return;
        }

        db.addUserEcole(userId, ecoleDB.getId());
        Toast.makeText(this,
                "✅ Inscrit à " + ecole.getNom() + " (" + (nbEcoles + 1) + "/" + limite + ")",
                Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Inscription: " + ecole.getNom() + " userId=" + userId);
    }

    private int getLimiteEcoles(String pack) {
        if (pack == null || pack.isEmpty()) return 0;
        switch (pack.trim()) {
            case "TAWJIH PLUS":    return 7;
            case "TAWJIH PRO":     return 15;
            case "TAWJIH PREMIUM": return 999;
            case "TASSJIL 7":      return 7;
            case "TASSJIL TOP 15": return 15;
            case "TASSJIL PLUS":   return 999;
            default:               return 0;
        }
    }

    private Ecole getOrCreateEcole(Ecole ecole) {
        List<Ecole> toutesEcoles = db.getAllEcoles();
        for (Ecole e : toutesEcoles) {
            if (e.getNom().equals(ecole.getNom())) {
                return e;
            }
        }
        long newId = db.addEcole(ecole);
        if (newId != -1) {
            ecole.setId(newId);
            return ecole;
        }
        return null;
    }

    private void loadEcolesData() {
        ecoleList.clear();

        List<Ecole> ecolesDB = db.getAllEcoles();
        if (!ecolesDB.isEmpty()) {
            ecoleList.addAll(ecolesDB);
            adapter.notifyDataSetChanged();
            return;
        }

        List<Ecole> defaultEcoles = getDefaultEcoles();
        for (Ecole e : defaultEcoles) {
            long id = db.addEcole(e);
            e.setId(id);
            ecoleList.add(e);
        }

        adapter.notifyDataSetChanged();
        Log.d(TAG, ecoleList.size() + " écoles chargées dans SQLite");
    }

    private List<Ecole> getDefaultEcoles() {
        List<Ecole> list = new ArrayList<>();

        list.add(new Ecole("EST Kénitra", "المدرسة العليا للتكنولوجيا بالقنيطرة", "Public", "Kénitra", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Casablanca", "المدرسة العليا للتكنولوجيا بالدار البيضاء", "Public", "Casablanca", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Rabat", "المدرسة العليا للتكنولوجيا بالرباط", "Public", "Rabat", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Fès", "المدرسة العليا للتكنولوجيا بفاس", "Public", "Fès", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Marrakech", "المدرسة العليا للتكنولوجيا بمراكش", "Public", "Marrakech", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Tanger", "المدرسة العليا للتكنولوجيا بطنجة", "Public", "Tanger", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Sidi Bennour", "المدرسة العليا للتكنولوجيا بسيدي بنور", "Public", "Sidi Bennour", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Oujda", "المدرسة العليا للتكنولوجيا بوجدة", "Public", "Oujda", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Meknès", "المدرسة العليا للتكنولوجيا بمكناس", "Public", "Meknès", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Agadir", "المدرسة العليا للتكنولوجيا بأكادير", "Public", "Agadir", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Settat", "المدرسة العليا للتكنولوجيا بسطات", "Public", "Settat", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST El Jadida", "المدرسة العليا للتكنولوجيا بالجديدة", "Public", "El Jadida", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Khouribga", "المدرسة العليا للتكنولوجيا بخريبكة", "Public", "Khouribga", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Béni Mellal", "المدرسة العليا للتكنولوجيا ببني ملال", "Public", "Béni Mellal", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Guelmim", "المدرسة العليا للتكنولوجيا بكلميم", "Public", "Guelmim", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Laayoune", "المدرسة العليا للتكنولوجيا بالعيون", "Public", "Laayoune", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Dakhla", "المدرسة العليا للتكنولوجيا بالداخلة", "Public", "Dakhla", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));
        list.add(new Ecole("EST Nador", "المدرسة العليا للتكنولوجيا بالناظور", "Public", "Nador", "Gratuit", "2 ans", "DUT", "Technologie", "15 Juillet 2026"));

        list.add(new Ecole("UM6P", "Université Mohammed VI Polytechnique", "Privé", "Benguerir / Salé", "72 500 - 78 750 DH", "5 ans", "Licence / Master", "Ingénierie, Sciences, Économie", "30 Août 2026"));
        list.add(new Ecole("UM6P-FGSES", "Faculté de Gouvernance, des Sciences Economiques et Sociales", "Privé", "Salé", "72 500 DH", "5 ans", "Licence / Master", "Gouvernance, Économie", "30 Août 2026"));
        list.add(new Ecole("UM6P-IST&I", "Institute of Science, Technology and Innovation", "Privé", "Benguerir", "78 750 DH", "5 ans", "Master / Doctorat", "Sciences, Technologie, Innovation", "30 Août 2026"));

        list.add(new Ecole("FMPR", "Faculté de Médecine et de Pharmacie de Rabat", "Public", "Rabat", "Gratuit", "7 ans", "Doctorat en Médecine", "Médecine", "25 Juin 2026"));
        list.add(new Ecole("FMPC", "Faculté de Médecine et de Pharmacie de Casablanca", "Public", "Casablanca", "Gratuit", "7 ans", "Doctorat en Médecine", "Médecine", "25 Juin 2026"));
        list.add(new Ecole("FMPF", "Faculté de Médecine et de Pharmacie de Fès", "Public", "Fès", "Gratuit", "7 ans", "Doctorat en Médecine", "Médecine", "25 Juin 2026"));
        list.add(new Ecole("FMPM", "Faculté de Médecine et de Pharmacie de Marrakech", "Public", "Marrakech", "Gratuit", "7 ans", "Doctorat en Médecine", "Médecine", "25 Juin 2026"));
        list.add(new Ecole("FMPO", "Faculté de Médecine et de Pharmacie d'Oujda", "Public", "Oujda", "Gratuit", "7 ans", "Doctorat en Médecine", "Médecine", "25 Juin 2026"));
        list.add(new Ecole("FMPA", "Faculté de Médecine et de Pharmacie d'Agadir", "Public", "Agadir", "Gratuit", "7 ans", "Doctorat en Médecine", "Médecine", "25 Juin 2026"));
        list.add(new Ecole("FMPT", "Faculté de Médecine et de Pharmacie de Tanger", "Public", "Tanger", "Gratuit", "7 ans", "Doctorat en Médecine", "Médecine", "25 Juin 2026"));

        list.add(new Ecole("FMD Rabat", "Faculté de Médecine Dentaire de Rabat", "Public", "Rabat", "Gratuit", "6 ans", "Doctorat en Médecine Dentaire", "Chirurgie Dentaire", "20 Juin 2026"));
        list.add(new Ecole("FMD Casablanca", "Faculté de Médecine Dentaire de Casablanca", "Public", "Casablanca", "Gratuit", "6 ans", "Doctorat en Médecine Dentaire", "Chirurgie Dentaire", "20 Juin 2026"));

        list.add(new Ecole("INAU", "Institut National d'Aménagement et d'Urbanisme", "Public", "Rabat", "Gratuit", "5 ans", "Master / Ingénieur", "Urbanisme, Aménagement", "10 Juillet 2026"));
        list.add(new Ecole("INAS", "Institut National de l'Action Sociale", "Public", "Tanger", "Gratuit", "3 ans", "Licence / Master", "Action Sociale", "12 Juillet 2026"));
        list.add(new Ecole("ISCAE", "Institut Supérieur de Commerce et d'Administration des Entreprises", "Public", "Casablanca / Rabat", "Gratuit", "5 ans", "Master / Cycle d'ingénieur", "Commerce, Gestion, Entrepreneuriat", "30 Juin 2026"));
        list.add(new Ecole("ISIC", "Institut Supérieur de l'Information et de la Communication", "Public", "Rabat", "Gratuit", "3-5 ans", "Licence / Master", "Journalisme, Communication", "05 Juillet 2026"));
        list.add(new Ecole("ISPITS", "Institut Supérieur des Professions Infirmières et Techniques de Santé", "Public", "Plusieurs villes", "Gratuit", "3 ans", "Licence / Master", "Soins Infirmiers, Santé", "28 Juin 2026"));

        list.add(new Ecole("ENSAM", "المدرسة الوطنية للفنون والمهن", "Grande École", "Casablanca / Meknès", "Gratuit", "5 ans", "Ingénieur d'État", "Génie mécanique", "20 Juin 2026"));
        list.add(new Ecole("EMI", "المدرسة المحمدية للمهندسين", "Grande École", "Rabat", "Gratuit", "3 ans", "Ingénieur d'État", "Génie & Sciences", "20 Juin 2026"));
        list.add(new Ecole("EHTP", "المدرسة الحسنية للأشغال العمومية", "Grande École", "Casablanca", "Gratuit", "5 ans", "Ingénieur d'État", "Génie civil", "20 Juin 2026"));
        list.add(new Ecole("INPT", "المعهد الوطني للبريد والمواصلات", "Grande École", "Rabat", "Gratuit", "3 ans", "Ingénieur d'État", "Télécom & IT", "20 Juin 2026"));
        list.add(new Ecole("INSEA", "المعهد الوطني للإحصاء", "Grande École", "Rabat", "Gratuit", "3 ans", "Ingénieur d'État", "Data & Statistique", "20 Juin 2026"));
        list.add(new Ecole("ESITH", "المدرسة العليا لصناعة النسيج", "Grande École", "Casablanca", "Gratuit", "5 ans", "Ingénieur d'État", "Textile & Logistique", "25 Juin 2026"));

        list.add(new Ecole("ENCG", "المدرسة الوطنية للتجارة والتسيير", "Grande École", "Plusieurs villes", "Gratuit", "5 ans", "Master", "Gestion", "10 Juillet 2026"));
        list.add(new Ecole("HEM", "Haute École de Management", "Privé", "Casablanca / Rabat / Marrakech", "30 000 - 60 000 DH", "5 ans", "Master", "Management", "30 Août 2026"));
        list.add(new Ecole("UIR", "Université Internationale de Rabat", "Privé", "Rabat", "60 000 - 120 000 DH", "3-5 ans", "Bachelor / Master", "Business & Ingénierie", "01 Septembre 2026"));

        list.add(new Ecole("UM5", "جامعة محمد الخامس", "Université", "Rabat", "Gratuit", "Licence + Master", "Université", "Toutes filières", "05 Septembre 2026"));
        list.add(new Ecole("UH2", "جامعة الحسن الثاني", "Université", "Casablanca", "Gratuit", "Licence + Master", "Université", "Sciences & Droit", "05 Septembre 2026"));
        list.add(new Ecole("UCA", "جامعة القاضي عياض", "Université", "Marrakech", "Gratuit", "Licence + Master", "Université", "Sciences", "05 Septembre 2026"));
        list.add(new Ecole("UEMF", "جامعة فاس الأوروبية", "Privé", "Fès", "50 000 - 100 000 DH", "3-5 ans", "Bachelor / Master", "Ingénierie", "25 Août 2026"));

        list.add(new Ecole("OFPPT", "التكوين المهني", "Public", "Tout le Maroc", "Gratuit", "1-2 ans", "Technicien", "Formation professionnelle", "05 Septembre 2026"));
        list.add(new Ecole("ENS", "المدرسة العليا للأساتذة", "Public", "Plusieurs villes", "Gratuit", "3-5 ans", "Professeur", "Éducation", "20 Juillet 2026"));
        list.add(new Ecole("FSJES", "كلية الحقوق والاقتصاد", "Public", "Plusieurs villes", "Gratuit", "3-5 ans", "Licence / Master", "Droit & Économie", "05 Septembre 2026"));
        list.add(new Ecole("FST", "كلية العلوم والتقنيات", "Public", "Plusieurs villes", "Gratuit", "Licence + Master", "Université", "Sciences", "05 Septembre 2026"));

        return list;
    }
}