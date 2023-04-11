package com.example.requirements_manager.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.User;
import com.example.requirements_manager.services.UserService;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        loginButton = findViewById(R.id.login_button);
        registerTextView = findViewById(R.id.register_textview);

        UserService userService = new UserService(this);

        loginButton.setOnClickListener(v -> {

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty()) {
                usernameEditText.setError("Preencha este campo!");
                usernameEditText.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Preencha este campo!");
                passwordEditText.requestFocus();
                return;
            }

            User user = new User();
            user.setName(username);
            user.setPassword(password);

            User loggedInUser = userService.loginWithUsernameAndPassword(user);

            if (loggedInUser != null) {
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //i.putExtra("loggedIn", true);
                MainActivity.loggedIn = true;
                startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this, "Usuário ou senha inválido!", Toast.LENGTH_SHORT).show();
                MainActivity.loggedIn = false;
            }
        });

        registerTextView.setOnClickListener(v -> {

            Intent signup = new Intent(this, SignupActivity.class);
            startActivity(signup);

        });
    }
}
