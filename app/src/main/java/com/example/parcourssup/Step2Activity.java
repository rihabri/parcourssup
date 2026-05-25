package com.example.parcourssup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Step2Activity extends AppCompatActivity {

    private EditText etEmail, etPassword, etNote, etNumeroScolaire;
    private RadioGroup rgNiveau, rgAnnee, rgFiliere;
    private CheckBox cbBacLibre;
    private Button btnTerminer;

    private String prenom, nom, age, telephone, ville;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);

        db = DBHelper.getInstance(this);

        prenom = getIntent().getStringExtra("prenom");
        nom = getIntent().getStringExtra("nom");
        age = getIntent().getStringExtra("age");
        telephone = getIntent().getStringExtra("telephone");
        ville = getIntent().getStringExtra("ville");

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etNote = findViewById(R.id.etNote);
        etNumeroScolaire = findViewById(R.id.etNumeroScolaire);

        rgNiveau = findViewById(R.id.rgNiveau);
        rgAnnee = findViewById(R.id.rgAnnee);
        rgFiliere = findViewById(R.id.rgFiliere);

        cbBacLibre = findViewById(R.id.cbBacLibre);
        btnTerminer = findViewById(R.id.btnTerminer);

        btnTerminer.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String note = etNote.getText().toString().trim();
            String numeroScolaire = etNumeroScolaire.getText().toString().trim();

            int niveauId = rgNiveau.getCheckedRadioButtonId();
            int anneeId = rgAnnee.getCheckedRadioButtonId();
            int filiereId = rgFiliere.getCheckedRadioButtonId();

            if (email.isEmpty() || password.isEmpty() || note.isEmpty() || numeroScolaire.isEmpty()
                    || niveauId == -1 || anneeId == -1 || filiereId == -1) {

                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rbNiveau = findViewById(niveauId);
            RadioButton rbAnnee = findViewById(anneeId);
            RadioButton rbFiliere = findViewById(filiereId);

            String bacLibre = cbBacLibre.isChecked() ? "Oui" : "Non";

            SharedPreferences prefs = getSharedPreferences("ParcoursSup", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("prenom", prenom);
            editor.putString("nom", nom);
            editor.putString("age", age);
            editor.putString("telephone", telephone);
            editor.putString("ville", ville);
            editor.putString("email", email);
            editor.putString("niveau", rbNiveau.getText().toString());
            editor.putString("annee", rbAnnee.getText().toString());
            editor.putString("filiere", rbFiliere.getText().toString());
            editor.putString("note", note);
            editor.putString("numeroScolaire", numeroScolaire);
            editor.putString("bacLibre", bacLibre);
            editor.apply();

            User user = new User();
            user.setPrenom(prenom);
            user.setNom(nom);
            user.setAge(age);
            user.setTelephone(telephone);
            user.setVille(ville);
            user.setEmail(email);

            long inserted = db.addUser(user, password);

            if (inserted == -1) {
                Toast.makeText(this,
                        "Erreur inscription (email déjà utilisé)",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            User savedUser = db.getUserByEmail(email);
            long userId = (savedUser != null) ? savedUser.getId() : -1;

            editor.putLong("userId", userId);
            editor.apply();

            Intent intent = new Intent(this, Step3Activity.class);

            intent.putExtra("prenom", prenom);
            intent.putExtra("nom", nom);
            intent.putExtra("age", age);
            intent.putExtra("telephone", telephone);
            intent.putExtra("ville", ville);
            intent.putExtra("email", email);
            intent.putExtra("userId", userId);

            intent.putExtra("note", note);
            intent.putExtra("numeroScolaire", numeroScolaire);
            intent.putExtra("niveau", rbNiveau.getText().toString());
            intent.putExtra("annee", rbAnnee.getText().toString());
            intent.putExtra("filiere", rbFiliere.getText().toString());
            intent.putExtra("bacLibre", bacLibre);

            startActivity(intent);
            finish();
        });
    }
}