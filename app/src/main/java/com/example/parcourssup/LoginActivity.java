package com.example.parcourssup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvCreateAccount;

    private SessionManager session;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        db = DBHelper.getInstance(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            Log.d("LOGIN", "email=" + email);
            Log.d("LOGIN", "password=" + password);

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(this,
                        "Remplir les champs",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            boolean isValid = db.checkUser(email, password);

            if (isValid) {


                User user = db.getUserByEmail(email);

                if (user != null) {

                    SharedPreferences prefs = getSharedPreferences("ParcoursSup", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("prenom", user.getPrenom());
                    editor.putString("nom", user.getNom());
                    editor.putString("email", user.getEmail());
                    editor.putString("telephone", user.getTelephone());
                    editor.putString("ville", user.getVille());
                    editor.putLong("userId", user.getId());
                    editor.apply();

                    Log.d("LOGIN", "Prénom sauvegardé: " + user.getPrenom());
                }

                session.loginUser(email);

                Toast.makeText(this,
                        "Login success",
                        Toast.LENGTH_SHORT).show();

                Intent intent =
                        new Intent(LoginActivity.this,
                                HomeActivity.class);

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                );

                startActivity(intent);

                finish();

            } else {

                Toast.makeText(this,
                        "Email ou mot de passe incorrect",
                        Toast.LENGTH_SHORT).show();
            }
        });

        tvCreateAccount.setOnClickListener(v -> {

            startActivity(
                    new Intent(LoginActivity.this,
                            Step1Activity.class)
            );
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (db != null)
            db.close();
    }
}