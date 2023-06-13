package com.example.fbapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        Button btnSignIn = findViewById(R.id.sign_in_button);
        Button btnSignUp = findViewById(R.id.sign_up_button);
        EditText inputEmail = findViewById(R.id.email);
        EditText inputPassword = findViewById(R.id.password);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button btnResetPassword = findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class)));

        btnSignIn.setOnClickListener(v -> finish());

        btnSignUp.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Entrer l'adresse e-mail!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Entrer le mot de passe!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Mot de passe trop court, entrez minimum 6 caractères !", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(SignupActivity.this, "Utilisateur créé avec succès" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Authentification échouée." + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        }
                    });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }
}
