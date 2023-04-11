package com.example.requirements_manager.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.Project;
import com.example.requirements_manager.entities.User;
import com.example.requirements_manager.services.ProjectService;
import com.example.requirements_manager.services.RequirementService;
import com.example.requirements_manager.services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private MenuHelper menuHelper;

    public static boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuHelper = new MenuHelper(this);

        mockUsers();

        //loggedIn = getIntent().getBooleanExtra("loggedIn", false);

        //ADICIONAR LOG OFF TODO

        if (!loggedIn) {

            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);

        }

        setProjectsView();

        Button createProjectBtn;

        createProjectBtn = findViewById(R.id.createProjectBtn);

        createProjectBtn.setOnClickListener(
                view -> {
                    Intent i = new Intent(MainActivity.this, ProjectActivity.class);
                    startActivityForResult(i, 1);
                }
            );

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION_PERMISSION && resultCode == 1) {
            recreate();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void setProjectsView() {

        ProjectService projectService = new ProjectService(this);

        RequirementService requirementService = new RequirementService(this);

        LinearLayout cardContainer = new LinearLayout(this);
        cardContainer.setOrientation(LinearLayout.VERTICAL);

        for (Project project : projectService.findAll()) {

            CardView cardView = new CardView(this);
            View view = getLayoutInflater().inflate(R.layout.card_view, null);
            cardView.addView(view);

            TextView nameView = cardView.findViewById(R.id.tv_name);
            nameView.setText(project.getName());

            Long weeks = ChronoUnit.WEEKS.between(project.getStartDate(), LocalDate.now());
            weeks = project.getStartDate().isAfter(LocalDate.now()) ? 0 : weeks;
            TextView weeksView = cardView.findViewById(R.id.tv_weeks);
            weeksView.setText("Semanas: " + (weeks != 0 ? weeks : "Não iniciado!"));

            TextView finishDateView = cardView.findViewById(R.id.tv_finish_date);
            finishDateView.setText("Data de entrega: " + project.getFinalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            TextView usernameView = cardView.findViewById(R.id.tv_username);
            usernameView.setText("Responsável: " + "admin");

            cardView.setOnClickListener(view1 -> {

                Intent i = new Intent(MainActivity.this, RequirementListActivity.class);
                i.putExtra("projectTitle", project.getName());
                i.putExtra("docUrl", project.getDocumentacaoUrl());
                i.putExtra("projectId", project.getId());
                startActivity(i);

            });

            cardContainer.addView(cardView);

        }

        ScrollView scrollView = findViewById(R.id.project_cards_view);
        scrollView.addView(cardContainer);

    }

    private void mockUsers() {

        UserService userService = new UserService(this);

        List<User> users = new ArrayList<>();

        users.add(new User(1, "admin", "poletto_softwares", "admin@gmail.com", "admin"));
        users.add(new User(2, "leonardo", "poletto_softwares", "leonardo@gmail.com", "123456"));
        users.add(new User(3, "juca bala", "VASP", "juca@bala.com", "balajuca"));


        if (userService.findAll().size() > 0) {
            userService.delete(1);
            userService.delete(2);
            userService.delete(3);
        }

        users.forEach(x -> userService.save(x));

        System.out.println(userService.findAll());

    }

}
