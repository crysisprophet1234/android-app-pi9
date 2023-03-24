package com.example.requirements_manager.application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.Requirement;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;
import java.util.Random;

public class RequirementsActivity extends AppCompatActivity {

    TextInputEditText title, desc, hours;

    Button back, clear, add;

    Spinner difficulty, importance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requirements_activity_main);

        setSpinners();

        title = findViewById(R.id.requirementsTitle);
        desc = findViewById(R.id.requirementDesc);
        hours = findViewById(R.id.hoursEstimated);

        back = findViewById(R.id.backBtn);
        clear = findViewById(R.id.clearBtn);
        add = findViewById(R.id.addBtn);

        difficulty = (Spinner) findViewById(R.id.requirementDifficulty);
        importance = (Spinner) findViewById(R.id.requirementImportance);

        setMockRequirement();

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if
                (
                    checkEmpty() && checkAll()
                )
                {

                    Requirement requirement = new Requirement();
                    requirement.setTitle(title.getText().toString());
                    requirement.setDesc(title.getText().toString());
                    requirement.setImportance(importance.getSelectedItem().toString());
                    requirement.setDifficulty(difficulty.getSelectedItem().toString());
                    requirement.setHours(Integer.parseInt(hours.getText().toString()));

                    //System.out.println(requirement.toString());

                    setValidations("Requisito adicionado com sucesso", "#DCDCDC", title);

                    ProjectActivity.requirementsInput.add(requirement);

                    clearAll();
                }
                else
                {
                    System.out.println("Falha!");
                }

            }

        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater exibirMenu = getMenuInflater();
        exibirMenu.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.homepage:

                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.project_activity:

                startActivity(new Intent(this, ProjectActivity.class));
                return true;

            case R.id.requirement_register:

                startActivity(new Intent(this, RequirementsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkEmpty() {

        if
        (
                title.getText().toString().isEmpty() ||
                desc.getText().toString().isEmpty() ||
                hours.getText().toString().isEmpty()

        )
        {
            setValidations("Todos campos devem estar preenchidos", "#DCDCDC", title);
            return false;
        }

        return true;

    }

    private boolean checkAll() {

        boolean valid = true;

        if (title.getText().toString().length() < 2) {
            setValidations("Campo título deve ser válido", "#dc3545", title);
            valid = false;
        } else {
            setValidations("", "#DCDCDC", title);
        }

        if (desc.getText().toString().length() < 20) {
            setValidations("Campo descrição deve ser válido", "#dc3545", desc);
            valid = false;
        } else {
            setValidations("", "#DCDCDC", desc);
        }

        if (!hours.getText().toString().matches("^[0-9]*$")) {
            setValidations("Campo horário deve ser válido", "#dc3545", hours);
            valid = false;
        } else {
            setValidations("", "#DCDCDC", hours);
        }

        return valid;
    }

    private void setValidations (String msg, String color, EditText field) {
        field.setBackgroundColor(Color.parseColor(color));
        if (!msg.equals("")) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, 2000);
            snackbar.show();
        }
    }

    private void setSpinners() {

        {

            Spinner spinnerImportance = (Spinner) findViewById(R.id.requirementImportance);

            ArrayAdapter<CharSequence> adapterImportance = ArrayAdapter.createFromResource(this,
                    R.array.importance_array, android.R.layout.simple_spinner_item);

            adapterImportance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerImportance.setAdapter(adapterImportance);

        }

        {

            Spinner spinnerDifficulty = (Spinner) findViewById(R.id.requirementDifficulty);

            ArrayAdapter<CharSequence> adapterDifficulty = ArrayAdapter.createFromResource(this,
                    R.array.difficulty_array, android.R.layout.simple_spinner_item);

            adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerDifficulty.setAdapter(adapterDifficulty);

        }

    }

    private void clearAll() {
        title.setText("");
        desc.setText("");
        hours.setText("");
        importance.setSelection(0);
        difficulty.setSelection(0);
    }
    private void setMockRequirement() {

        TextView mockRequirement = findViewById(R.id.secret_mock_requirement);

        mockRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random random = new Random();

                int randomInt = random.nextInt(3);

                switch (randomInt) {

                    case 0:
                        title.setText("Funcionar em Android");
                        desc.setText("O aplicativo deverá conter as tecnologias capazes de operar em sistema Android");
                        hours.setText("50");
                        importance.setSelection(2);
                        difficulty.setSelection(2);
                        break;

                    case 1:
                        title.setText("Permitir cadastro de projetos e seus requisitos");
                        desc.setText("O aplicativo deverá ser capaz de armazenar projetos, responsáveis e requisitos");
                        hours.setText("30");
                        importance.setSelection(1);
                        difficulty.setSelection(0);
                        break;

                    case 2:
                        title.setText("Disponibilidade para usuários");
                        desc.setText("Suporte a pelo menos 500 usuários simultâneos");
                        hours.setText("80");
                        importance.setSelection(2);
                        difficulty.setSelection(0);
                        break;

                    case 3:
                        title.setText("Suporte a múltiplos idiomas");
                        desc.setText("O sistema deverá ser traduzido e modo que os falantes dos prinicipais idiomas consigam utilizar");
                        hours.setText("150");
                        importance.setSelection(2);
                        difficulty.setSelection(1);
                        break;

                }

            }
        });

    }

}
