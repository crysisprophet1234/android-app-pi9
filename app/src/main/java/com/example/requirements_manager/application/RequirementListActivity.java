package com.example.requirements_manager.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.services.RequirementService;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class RequirementListActivity extends AppCompatActivity {

    TextView tvTitle, tvLink;
    Button returnRequirementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_list);

        setUpRequirements();

        returnRequirementList = findViewById(R.id.returnRequirementList);

        returnRequirementList.setOnClickListener(new View.OnClickListener() {
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

    private void setUpRequirements() {

        RequirementService service = new RequirementService(this);

        tvTitle = findViewById(R.id.titleRequirementsList);
        tvTitle.setText(getIntent().getStringExtra("projectTitle"));

        tvLink = findViewById(R.id.linkText);
        String link = getIntent().getStringExtra("docUrl");
        if (link != null) {
            tvLink.setOnClickListener(view -> {
                Intent i = new Intent(this, DocumentationActivity.class);
                i.putExtra("docUrl", link);
                startActivity(i);
            });
        }

        Integer projectId = getIntent().getIntExtra("projectId", 0);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        Boolean isCreating = getIntent().getBooleanExtra("isCreating", false);

        List<Requirement> requirements = isCreating ? ProjectActivity.requirementsInput : service.findByProjectId(Long.valueOf(projectId));

        for (Requirement req : requirements) {

            CardView cardView = new CardView(this);

            CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(0, 0, 0, 64);

            cardView.setLayoutParams(layoutParams);

            cardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
            cardView.setCardElevation(8f);
            cardView.setRadius(16f);
            cardView.setContentPadding(16, 20, 20, 16);

            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(8, 16, 8, 16);

            {

                LinearLayout titleIdLayout = new LinearLayout(this);
                titleIdLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                titleIdLayout.setOrientation(LinearLayout.HORIZONTAL);
                titleIdLayout.setWeightSum(2);

                TextView titleTextView = new TextView(this);
                titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                titleTextView.setText(req.getTitle());
                titleTextView.setTextSize(18f);
                titleTextView.setMaxWidth(450);
                titleTextView.setTextColor(getResources().getColor(R.color.black));
                titleIdLayout.addView(titleTextView);

                TextView idTextView = new TextView(this);
                idTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                if (req.getId() != null) {
                    idTextView.setText("ID " + req.getId().toString());
                }
                idTextView.setTextSize(14f);
                idTextView.setGravity(Gravity.END);
                titleIdLayout.addView(idTextView);

                cardLayout.addView(titleIdLayout);

            }

            {

                LinearLayout datePriorityLayout = new LinearLayout(this);

                LinearLayout.LayoutParams datePriorityLinearLayout = new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);

                datePriorityLinearLayout.setMargins(0, 24, 0, 16);

                datePriorityLayout.setLayoutParams(datePriorityLinearLayout);
                datePriorityLayout.setOrientation(LinearLayout.HORIZONTAL);
                datePriorityLayout.setWeightSum(2);

                TextView dueDateTextView = new TextView(this);
                dueDateTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                if (req.getMoment() != null) {
                    dueDateTextView.setText("Criado em: " + formatDate(req.getMoment().toString().substring(0, 10)));
                }
                datePriorityLayout.addView(dueDateTextView);

                TextView priorityTextView = new TextView(this);
                priorityTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                priorityTextView.setText(req.getImportance());
                priorityTextView.setGravity(Gravity.END);
                datePriorityLayout.addView(priorityTextView);

                cardLayout.addView(datePriorityLayout);

            }

            {

                TextView descriptionTextView = new TextView(this);

                LinearLayout.LayoutParams descLayoutParams = new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);

                descLayoutParams.setMargins(0, 20, 0, 16);

                descriptionTextView.setLayoutParams(descLayoutParams);

                descriptionTextView.setText(req.getDesc());
                cardLayout.addView(descriptionTextView);

            }

            cardLayout.setOnClickListener(view -> {

                Intent i = new Intent(this, RequirementsActivity.class);
                i.putExtra("isUpdating", true);
                i.putExtra("requirementId", req.getId());
                i.putExtra("projectInputName", req.getProject().getName());
                startActivityForResult(i, 1);

            });

            cardView.addView(cardLayout);

            linearLayout.addView(cardView);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            recreate();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Requisito atualizado com sucesso!", 1500);
            snackbar.show();
        }
    }

    private String formatDate(String substring) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(substring, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String outputString = date.format(outputFormatter);
        return outputString;

    }
}