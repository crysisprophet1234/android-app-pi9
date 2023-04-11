package com.example.requirements_manager.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.User;
import com.example.requirements_manager.services.UserService;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextName, editTextCompany, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister, buttonVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextCompany = findViewById(R.id.editTextCompany);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> validateAndRegister());

        buttonVoltar = findViewById(R.id.buttonReturn);
        buttonVoltar.setOnClickListener(view -> finish());

    }

    private void validateAndRegister() {
        String name = editTextName.getText().toString().trim();
        String company = editTextCompany.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Preencha o campo nome");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Preencha o campo email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email deve ser válido");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Preencha o campo senha");
            editTextPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Confirme a senha");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Senhas não coincidem");
            editTextConfirmPassword.requestFocus();
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setCompany(company);
        user.setPassword(password);

        UserService userService = new UserService(this);

        User insertedUser = userService.save(user);

        if (insertedUser.getId() == null) {
            Toast.makeText(this, "Erro no cadastro de usuário", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            System.out.println("usuario cadastrado -> " + insertedUser);
            clearFields();
        }
    }

    private void clearFields() {
        editTextName.setText("");
        editTextCompany.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");
    }

}
