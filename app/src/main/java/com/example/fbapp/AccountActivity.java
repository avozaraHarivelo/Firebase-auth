package com.example.fbapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountActivity extends AppCompatActivity {
    private Button changeEmail;
    private Button changePassword;
    private Button sendEmail;
    private Button remove;
    private EditText oldEmail;
    private EditText newEmail;
    private EditText password;
    private EditText newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        initializeViews();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        authListener = firebaseAuth -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser == null) {
                navigateToLogin();
            }
        };

        Button btnChangeEmail = findViewById(R.id.change_email_button);
        Button btnSendResetEmail = findViewById(R.id.sending_pass_reset_button);
        Button btnRemoveUser = findViewById(R.id.remove_user_button);
        Button signOut = findViewById(R.id.sign_out);

        setVisibilityOfViews();

        btnChangeEmail.setOnClickListener(v -> showChangeEmailViews());
        changeEmail.setOnClickListener(v -> changeEmail());
        btnSendResetEmail.setOnClickListener(v -> showResetPasswordViews());
        sendEmail.setOnClickListener(v -> sendPasswordResetEmail());
        btnRemoveUser.setOnClickListener(v -> removeUser());
        signOut.setOnClickListener(v -> signOut());
    }

    private void initializeViews() {
        changeEmail = findViewById(R.id.changeEmail);
        changePassword = findViewById(R.id.changePass);
        sendEmail = findViewById(R.id.send);
        remove = findViewById(R.id.remove);
        oldEmail = findViewById(R.id.old_email);
        newEmail = findViewById(R.id.new_email);
        password = findViewById(R.id.password);
        newPassword = findViewById(R.id.newPassword);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setVisibilityOfViews() {
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void navigateToLogin() {
        startActivity(new Intent(AccountActivity.this, LoginActivity.class));
        finish();
    }

    private void showChangeEmailViews() {
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.VISIBLE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.VISIBLE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
    }

    private void changeEmail() {
        progressBar.setVisibility(View.VISIBLE);
        if (user != null && !newEmail.getText().toString().trim().equals("")) {
            user.updateEmail(newEmail.getText().toString().trim())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AccountActivity.this, "L'adresse e-mail est mise à jour. Veuillez vous connecter avec un nouvel e-mail !", Toast.LENGTH_LONG).show();
                            signOut();
                        } else {
                            Toast.makeText(AccountActivity.this, "Échec de la mise à jour de l'e-mail !", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    });
        } else if (newEmail.getText().toString().trim().equals("")) {
            newEmail.setError("Entrez l'e-mail");
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showResetPasswordViews() {
        oldEmail.setVisibility(View.VISIBLE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.VISIBLE);
        remove.setVisibility(View.GONE);
    }

    private void sendPasswordResetEmail() {
        progressBar.setVisibility(View.VISIBLE);
        if (!oldEmail.getText().toString().trim().equals("")) {
            auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AccountActivity.this, "Réinitialiser le mot de passe. le mail est envoyé !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountActivity.this, "Échec de l'envoi de l'e-mail de réinitialisation !", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    });
        } else {
            oldEmail.setError("Entrez l'e-mail");
            progressBar.setVisibility(View.GONE);
        }
    }

    private void removeUser() {
        progressBar.setVisibility(View.VISIBLE);
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AccountActivity.this, "Votre profil est supprimé :( Créez un compte maintenant !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AccountActivity.this, SignupActivity.class));
                            finish();
                        } else {
                            Toast.makeText(AccountActivity.this, "Échec de la suppression de votre compte !", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    });
        }
    }

    private void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}