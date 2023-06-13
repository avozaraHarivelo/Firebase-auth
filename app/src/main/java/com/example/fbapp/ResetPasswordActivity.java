package com.example.fbapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EditText inputEmail = findViewById(R.id.email);
        Button btnReset = findViewById(R.id.btn_reset_password);
        Button btnBack = findViewById(R.id.btn_back);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(v -> finish());

        btnReset.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Entrez votre identifiant de messagerie enregistré", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Nous vous avons envoyé des instructions pour réinitialiser votre mot de passe !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Échec de l'envoi de l'e-mail de réinitialisation !", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    });
        });
    }
}
