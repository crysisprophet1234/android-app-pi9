package com.example.requirements_manager.application;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.requirements_manager.R;
import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.entities.RequirementImage;
import com.example.requirements_manager.services.RequirementImageService;
import com.example.requirements_manager.services.RequirementService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RequirementsActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE = 1;
    TextInputEditText title, desc, hours;
    Button back, clear, add, btnChooseFile;
    Spinner difficulty, importance;
    Set<String> imagesInputSet = new HashSet<>(2);

    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.requirements_activity_main);

        menuHelper = new MenuHelper(this);

        setSpinners();

        title = findViewById(R.id.requirementsTitle);
        desc = findViewById(R.id.requirementDesc);
        hours = findViewById(R.id.hoursEstimated);

        back = findViewById(R.id.backBtn);
        clear = findViewById(R.id.clearBtn);
        add = findViewById(R.id.addBtn);
        btnChooseFile = findViewById(R.id.btnChooseFile);

        difficulty = findViewById(R.id.requirementDifficulty);
        importance = findViewById(R.id.requirementImportance);

        setMockRequirement();

        add.setOnClickListener(view -> {

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

                for (String imgPath : imagesInputSet) {

                    RequirementImage reqImg = new RequirementImage();
                    reqImg.setUrl(imgPath);
                    reqImg.setRequirement(requirement);
                    requirement.getImages().add(reqImg);

                }

                //System.out.println(requirement.toString());

                setValidations("Requisito adicionado com sucesso", "#DCDCDC", title);

                ProjectActivity.requirementsInput.add(requirement);

                clearAll();
            }
            else
            {
                Toast.makeText(this, "Algo deu errado!", Toast.LENGTH_SHORT).show();
            }

        });

        clear.setOnClickListener(view -> clearAll());

        back.setOnClickListener(view -> finish());

        btnChooseFile.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);

        });

        checkIntent();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_REQUEST_CODE && imagesInputSet.size() < 2) {

            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);

                System.out.println(picturePath);

                cursor.close();

                if (!imagesManager(picturePath, 2)) {
                    setUpInputImage(picturePath);
                } else {
                    Toast.makeText(this, "Imagem já adicionada!", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private void setUpInputImage(String picturePath) {

        LinearLayout layout = findViewById(R.id.image_container);

        LinearLayout imageLayout = new LinearLayout(this);
        imageLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView img = new ImageView(this);

        Button deleteButton = new Button(this);

        LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imgLayoutParams.weight = 1;

        imgLayoutParams.setMarginEnd(15);
        imgLayoutParams.setMarginStart(15);

        img.setLayoutParams(imgLayoutParams);

        img.setImageURI(Uri.parse(picturePath));

        img.setTag(picturePath);

        ViewTreeObserver viewTreeObserver = img.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                img.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int imageWidth = img.getWidth();
                deleteButton.setWidth(imageWidth);
            }
        });

        img.setOnClickListener(view -> {

            String imagePath = (String) img.getTag();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(imagePath), "image/*");
            startActivity(intent);

        });

        imageLayout.addView(img);

        deleteButton.setText("Delete");
        imgLayoutParams.setMargins(0, 10, 0, 0);
        deleteButton.setLayoutParams(imgLayoutParams);

        deleteButton.setOnClickListener(view -> {
            layout.removeView(imageLayout);
            imagesManager(picturePath, 1);
        });

        imageLayout.addView(deleteButton);

        layout.addView(imageLayout);

        imagesManager(picturePath, 0);

    }

    private boolean imagesManager(String picturePath, int action) {

        System.out.println("imagesManager action " + action);

        if (action == 0) {

            imagesInputSet.removeIf(x -> x.equals(picturePath));

            imagesInputSet.add(picturePath);

        } else if (action == 1) {

            imagesInputSet.removeIf(x -> x.equals(picturePath));

        } else if (action == 2) {

            return imagesInputSet.contains(picturePath);

        }

        System.out.println("images -> " + imagesInputSet);
        return false;

    }

    private void checkIntent() {

        Intent intent = getIntent();

        if (intent != null && intent.getBooleanExtra("isUpdating", false)) {

            RequirementService reqService = new RequirementService(this);
            Requirement req = reqService.findById(Long.valueOf(intent.getIntExtra("requirementId", 0)));

            RequirementImageService reqImageService = new RequirementImageService(this);

            imagesInputSet.retainAll(reqImageService.findByRequirementId(Long.valueOf(req.getId())));

            TextView mockRequirement = findViewById(R.id.secret_mock_requirement);

            mockRequirement.setText(getIntent().getStringExtra("projectInputName"));

            mockRequirement.setOnClickListener(null);

            title.setText(req.getTitle());
            desc.setText(req.getDesc());
            hours.setText(String.valueOf(req.getHours()));

            System.out.print("imagensInput = reqImageService.find() = ");
            System.out.println(reqImageService.findByRequirementId(Long.valueOf(req.getId())));

            reqImageService.findByRequirementId(Long.valueOf(req.getId()))
                    .forEach(x -> setUpInputImage(x.getUrl()));

            for (int i = 0; i < difficulty.getAdapter().getCount(); i++) {
                String itemText = difficulty.getAdapter().getItem(i).toString();
                if (itemText.equals(req.getDifficulty())) {
                    difficulty.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < importance.getAdapter().getCount(); i++) {
                String itemText = importance.getAdapter().getItem(i).toString();
                if (itemText.equals(req.getImportance())) {
                    importance.setSelection(i);
                    break;
                }
            }

            add.setText("ATUALIZAR");

            add.setOnClickListener(null);

            add.setOnClickListener(view -> {

                if
                (
                        checkEmpty() && checkAll()
                )
                {

                    Requirement newRequirement = new Requirement();
                    newRequirement.setId(req.getId());
                    newRequirement.setTitle(title.getText().toString());
                    newRequirement.setDesc(title.getText().toString());
                    newRequirement.setImportance(importance.getSelectedItem().toString());
                    newRequirement.setDifficulty(difficulty.getSelectedItem().toString());
                    newRequirement.setHours(Integer.parseInt(hours.getText().toString()));

                    reqImageService.findByRequirementId(Long.valueOf(req.getId()))
                            .forEach(x -> reqImageService.delete(Long.valueOf(x.getId())));

                    for (String imagePath : imagesInputSet) {

                        RequirementImage reqImage = new RequirementImage();
                        reqImage.setUrl(imagePath);
                        reqImage.setRequirement(newRequirement);
                        reqImageService.save(reqImage);

                    }

                    setValidations("Requisito atualizado com sucesso", "#DCDCDC", title);

                    reqService.update(newRequirement);

                    setResult(1);

                    finish();

                }
                else
                {
                    System.out.println("Falha!");
                }

            });

            clear.setEnabled(false);

            back.setOnClickListener(null);

            back.setOnClickListener(view -> finish());

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
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, 1000);
            snackbar.show();
        }
    }

    private void setSpinners() {

        {

            Spinner spinnerImportance = findViewById(R.id.requirementImportance);

            ArrayAdapter<CharSequence> adapterImportance = ArrayAdapter.createFromResource(this,
                    R.array.importance_array, android.R.layout.simple_spinner_item);

            adapterImportance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerImportance.setAdapter(adapterImportance);

        }

        {

            Spinner spinnerDifficulty = findViewById(R.id.requirementDifficulty);

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

        LinearLayout linearLayout = findViewById(R.id.image_container);
        linearLayout.removeAllViews();

        imagesInputSet.clear();

    }
    private void setMockRequirement() {

        TextView mockRequirement = findViewById(R.id.secret_mock_requirement);

        mockRequirement.setText(getIntent().getStringExtra("projectInputName"));

        mockRequirement.setOnClickListener(view -> {

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

        });

    }

}
