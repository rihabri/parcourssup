package com.example.parcourssup;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Step1Activity extends AppCompatActivity {

    private EditText etPrenom, etNom, etAge, etTelephone;
    private Spinner spinnerVille;
    private Button btnSuivant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);

        etPrenom = findViewById(R.id.etPrenom);
        etNom = findViewById(R.id.etNom);
        etAge = findViewById(R.id.etAge);
        etTelephone = findViewById(R.id.etTelephone);
        spinnerVille = findViewById(R.id.spinnerVille);
        btnSuivant = findViewById(R.id.btnSuivant);

        etTelephone.setInputType(InputType.TYPE_CLASS_PHONE);
        etTelephone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        String[] villes = {
                "Sélectionnez votre ville",
                "Casablanca", "Rabat", "Marrakech", "Fès",
                "Tanger", "Agadir", "Meknès", "Oujda",
                "Tétouan", "El Jadida", "Kenitra"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        villes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVille.setAdapter(adapter);

        btnSuivant.setOnClickListener(v -> {

            String prenom = etPrenom.getText().toString().trim();
            String nom = etNom.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String telephone = etTelephone.getText().toString().trim();
            String ville = spinnerVille.getSelectedItem().toString();

            if (prenom.isEmpty() || nom.isEmpty() || age.isEmpty() || telephone.isEmpty()) {
                Toast.makeText(this,
                        "Remplir tous les champs",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (ville.equals("Sélectionnez votre ville")) {
                Toast.makeText(this,
                        "Sélectionnez une ville",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!telephone.matches("^(06|07)[0-9]{8}$")) {
                Toast.makeText(this,
                        "Numéro invalide (ex: 0612345678)",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, Step2Activity.class);

            intent.putExtra("prenom", prenom);
            intent.putExtra("nom", nom);
            intent.putExtra("age", age);
            intent.putExtra("telephone", telephone);
            intent.putExtra("ville", ville);

            startActivity(intent);
        });
    }
}