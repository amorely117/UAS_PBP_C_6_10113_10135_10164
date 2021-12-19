package com.aurelia_truly_jessica.transvice_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aurelia_truly_jessica.transvice_test.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout txtEmail, txtName, txtPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.inputLayoutEmail);
        txtName = findViewById(R.id.inputLayoutName);
        txtPassword = findViewById(R.id.inputLayoutPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnClear = findViewById(R.id.btnClear);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail.getEditText().setText("");
                txtName.getEditText().setText("");
                txtPassword.getEditText().setText("");
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String name = txtName.getEditText().getText().toString().trim();
        String email = txtEmail.getEditText().getText().toString().trim();
        String password = txtPassword.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            txtName.setError("Name is required");
            txtName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Please provide valid email");
            txtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            txtPassword.setError("Minimal password length is 8");
            txtPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, email, password);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Register fail!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Register fail!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}