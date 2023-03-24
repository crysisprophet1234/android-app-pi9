package com.example.requirements_manager.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setProjectsView();

        Button createProjectBtn;

        mockUsers();

        createProjectBtn = findViewById(R.id.createProjectBtn);

        createProjectBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MainActivity.this, ProjectActivity.class);
                            startActivity(i);
                        }
                    }
            );

        }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater exibirMenu = getMenuInflater();
        exibirMenu.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.homepage:
                Intent homeIntent = new Intent(this, MainActivity.class);
                if (getClass().getName().equals(MainActivity.class.getName())) {
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(homeIntent);
                return true;

            case R.id.project_activity:

                Intent projectIntent = new Intent(this, ProjectActivity.class);
                if (getClass().getName().equals(ProjectActivity.class.getName())) {
                    projectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(projectIntent);
                return true;

            case R.id.requirement_register:

                Intent requirementIntent = new Intent(this, RequirementsActivity.class);
                if (getClass().getName().equals(RequirementsActivity.class.getName())) {
                    requirementIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(requirementIntent);
                return true;

                /*
            case R.id.deleteAll:

                RequirementService reqService = new RequirementService(this);
                ProjectService projectService = new ProjectService(this);

                reqService.deleteAll();
                projectService.deleteAll();

                 */

            default:
                return super.onOptionsItemSelected(item);
        }
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

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, RequirementListActivity.class);
                    i.putExtra("projectTitle", project.getName());
                    i.putExtra("projectId", project.getId());
                    startActivity(i);
                }
            });

            cardContainer.addView(cardView);

        }

        ScrollView scrollView = findViewById(R.id.project_cards_view);
        scrollView.addView(cardContainer);

    }

    private void mockUsers() {

        UserService userService = new UserService(this);

        List<User> users = new ArrayList<>();

        /*

        users.add(new User(1, "admin", "poletto_softwares", "admin@gmail.com", "admin"));
        users.add(new User(2, "leonardo", "poletto_softwares", "leonardo@gmail.com", "123456"));
        users.add(new User(3, "juca bala", "VASP", "juca@bala.com", "balajuca"));

         */

        userService.findAll().forEach(System.out::println);

    }

}
