package com.example.requirements_manager.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.Project;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProjectActivity extends AppCompatActivity {

    TextInputEditText projectName, projectStartDate, projectFinishDate;
    Integer i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_activity_main);

        projectName = findViewById(R.id.projectName);
        projectStartDate = findViewById(R.id.projectStartDate);
        projectFinishDate = findViewById(R.id.projectFinishDate);

        Button projectDetailsBtn = findViewById(R.id.projectDetailsBtn);

        projectDetailsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if

                (
                    checkAllFields() &&
                    checkName(projectName.getText().toString()) &&
                    checkStartDate(projectStartDate.getText().toString()) &&
                    checkFinalDate(projectFinishDate.getText().toString())
                )

                {

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    i = i++;
                    Project project = new Project();
                    project.setId(i);
                    project.setName(projectName.getText().toString());
                    project.setStartDate(LocalDate.parse(projectStartDate.getText().toString(), dtf));
                    project.setFinalDate(LocalDate.parse(projectFinishDate.getText().toString(), dtf));
                    System.out.println(project.toString());

                    Intent i = new Intent(ProjectActivity.this, RequirementsActivity.class);
                    startActivity(i);

                }

                else

                {

                    System.out.println("falha!!!!");

                }

            }

        });

    }

    private boolean checkAllFields () {
        if
        (
            this.projectName.getText().toString().trim().isEmpty() ||
            this.projectStartDate.getText().toString().trim().isEmpty() ||
            this.projectFinishDate.getText().toString().trim().isEmpty()
        )

        {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Todos campos devem estar preenchidos!", 10000);
            snackbar.show();
            return false;
        }

        else

        {

            return true;

        }
    }

    private boolean checkName(String str) {

        if ( str.length() > 2 && str.replace(" ", "").matches("[a-zA-Z]+") ) {
            this.projectName.setBackgroundColor(Color.parseColor("#DCDCDC"));
            return true;
        } else {
            this.projectName.setBackgroundColor(Color.parseColor("#dc3545"));
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Nome deve ser existente e não conter números!", 10000);
            snackbar.show();
            return false;
        }

    }

    private boolean checkStartDate(String dateStr) {

        LocalDate today = LocalDate.now();
        LocalDate date;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {

            date = LocalDate.parse(dateStr, dtf);

            if ( date.isAfter(today) || date.isEqual(today) ) {
                this.projectStartDate.setBackgroundColor(Color.parseColor("#DCDCDC"));
                return true;
            } else {
                this.projectStartDate.setBackgroundColor(Color.parseColor("#dc3545"));
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Data de ínicio deve ser maior ou igual à hoje!", 10000);
                snackbar.show();
            }

        } catch (Exception ex) {
            this.projectStartDate.setBackgroundColor(Color.parseColor("#dc3545"));
            Snackbar snackbar = Snackbar.make(this.projectStartDate, "Data inicial inválida!", 10000);
            snackbar.show();
            System.out.println(ex);
        }

        return false;

    }

    private boolean checkFinalDate(String dateStr) {

        String startDateStr = this.projectStartDate.getText().toString();
        LocalDate startDate;
        LocalDate finishDate;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {

            startDate = LocalDate.parse(startDateStr, dtf);
            finishDate = LocalDate.parse(dateStr, dtf);

            System.out.println(startDate);
            System.out.println(finishDate);



            if (finishDate.isAfter(startDate.plusMonths(1))) {
                this.projectFinishDate.setBackgroundColor(Color.parseColor("#DCDCDC"));
                return true;
            } else {
                this.projectFinishDate.setBackgroundColor(Color.parseColor("#dc3545"));
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Data final deve ser maior que data de ínicio", 10000);
                snackbar.show();
            }

        } catch (Exception ex) {
            this.projectFinishDate.setBackgroundColor(Color.parseColor("#dc3545"));
            Snackbar snackbar = Snackbar.make(this.projectFinishDate, "Data final inválida!", 10000);
            snackbar.show();
            System.out.println(ex);
        }

        return false;

    }

}
