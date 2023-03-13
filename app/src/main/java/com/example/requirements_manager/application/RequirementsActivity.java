package com.example.requirements_manager.application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.Requirement;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;

public class RequirementsActivity extends AppCompatActivity {

    TextInputEditText title, desc, hours;

    Button back, clear, add, finish;

    Spinner difficulty, importance;

    Integer i = 0;

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
        finish = findViewById(R.id.finishBtn);

        difficulty = (Spinner) findViewById(R.id.requirementDifficulty);
        importance = (Spinner) findViewById(R.id.requirementImportance);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if
                (
                    checkEmpty() && checkAll()
                )
                {
                    i = i++;
                    Requirement requirement = new Requirement();
                    requirement.setId(i);
                    requirement.setTitle(title.getText().toString());
                    requirement.setDesc(title.getText().toString());
                    requirement.setImportance(importance.getSelectedItem().toString());
                    requirement.setDifficulty(difficulty.getSelectedItem().toString());
                    requirement.setMoment(Instant.now());
                    requirement.setHours(Integer.parseInt(hours.getText().toString()));

                    System.out.println(requirement.toString());

                    setValidations("Requisito adicionado com sucesso", "#DCDCDC", title);
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
                Intent i = new Intent(RequirementsActivity.this, ProjectActivity.class);
                startActivity(i);
            }
        });

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
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, 7500);
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

}
