package com.example.requirements_manager.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.Project;
import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.services.ProjectService;
import com.example.requirements_manager.services.RequirementImageService;
import com.example.requirements_manager.services.RequirementService;
import com.example.requirements_manager.services.UserService;
import com.example.requirements_manager.util.LocationTracker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProjectActivity extends AppCompatActivity {

    public static List<Requirement> requirementsInput = new ArrayList<>();
    TextInputEditText projectName, projectStartDate, projectFinishDate, projectDocUrl;
    Button projectDetailsBtn, finishProjectBtn, listRequirementsBtn;
    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_activity_main);

        menuHelper = new MenuHelper(this);

        setTestMock();

        projectName = findViewById(R.id.projectName);
        projectDocUrl = findViewById(R.id.projectDocUrl);
        projectStartDate = findViewById(R.id.projectStartDate);
        projectFinishDate = findViewById(R.id.projectFinishDate);

        projectDetailsBtn = findViewById(R.id.projectDetailsBtn);
        finishProjectBtn = findViewById(R.id.finishProjectBtn);
        listRequirementsBtn = findViewById(R.id.listRequirementsBtn);

        projectDetailsBtn.setOnClickListener(view -> {

            if

            (
                checkAllFields() &&
                checkName(projectName.getText().toString()) &&
                checkStartDate(projectStartDate.getText().toString()) &&
                checkFinalDate(projectFinishDate.getText().toString())
            )

            {

                Intent i = new Intent(ProjectActivity.this, RequirementsActivity.class);
                i.putExtra("projectInputName", projectName.getText().toString());
                startActivity(i);

            }

            else

            {

                System.out.println("falha!!!!");

            }

        });

        listRequirementsBtn.setOnClickListener(view -> {

            Intent i = new Intent(this, RequirementListActivity.class);
            i.putExtra("isCreating", true);
            startActivity(i);

        });

        finishProjectBtn.setOnClickListener(view -> {

            if

            (
                    checkAllFields() &&
                    checkName(projectName.getText().toString()) &&
                    checkStartDate(projectStartDate.getText().toString()) &&
                    checkFinalDate(projectFinishDate.getText().toString()) &&
                    checkRequirements()
                    //TODO checkDocUrl()
            )

            {



                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                Project inputProject = new Project();
                inputProject.setName(projectName.getText().toString());
                inputProject.setDocumentacaoUrl(projectDocUrl.getText().toString());
                inputProject.setStartDate(LocalDate.parse(projectStartDate.getText().toString(), dtf));
                inputProject.setFinalDate(LocalDate.parse(projectFinishDate.getText().toString(), dtf));

                ProjectService projectService = new ProjectService(ProjectActivity.this);

                //TODO mocking user here!!!!
                inputProject.setUser(new UserService(ProjectActivity.this).findById(1));

                Project createdProject = projectService.save(inputProject);

                requirementsInput.forEach(x -> x.setProject(projectService.findById(createdProject.getId())));

                requirementsInput.forEach(x -> x.setLatitude(LocationTracker.getCurrentLocation(this)[0]));

                requirementsInput.forEach(x -> x.setLongitude(LocationTracker.getCurrentLocation(this)[1]));

                RequirementService requirementService = new RequirementService(ProjectActivity.this);

                requirementsInput = requirementService.saveAll(requirementsInput);

                RequirementImageService requirementImageService = new RequirementImageService(ProjectActivity.this);

                requirementsInput.forEach(x -> requirementImageService.saveAll(x.getImages()));

                System.out.println(createdProject);

                requirementsInput.forEach(System.out::println);

                requirementsInput.clear();

                setResult(1);

                finish();

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuHelper.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuHelper.onOptionsItemSelected(item);
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

    private boolean checkRequirements() {

        if (requirementsInput.size() < 3) {

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Devem haver pelo menos 3 requisitos!", 5000);
            snackbar.show();
            return false;

        }

        return true;

    }

    private boolean checkDocUrl() {

        String url = projectDocUrl.getText().toString();

        AtomicBoolean action = new AtomicBoolean(false);

        if (url.isEmpty()) {

            String a;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("AlertDialog");
            builder.setMessage("Would you like to continue learning how to use Android alerts?");

            builder.setPositiveButton("OK", (dialog, which) -> action.set(true));

            builder.setNegativeButton("Cancel", (dialog, which) -> action.set(false));

            AlertDialog dialog = builder.create();

            dialog.show();

        }

        System.out.println("returning action as " + action);
        return action.get();

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

    private void setTestMock() {

        TextView mockTest = findViewById(R.id.secret_mock_project);
        mockTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectName.setText("Projeto para Teste");
                projectStartDate.setText("15/07/2029");
                projectFinishDate.setText("28/03/2035");
                projectDocUrl.setText("https://github.com/crysisprophet1234/android-app-pi9");
            }
        });

    }

}
