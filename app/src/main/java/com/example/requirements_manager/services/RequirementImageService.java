package com.example.requirements_manager.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.requirements_manager.R;
import com.example.requirements_manager.database.SQLiteHelper;
import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.entities.RequirementImage;
import com.example.requirements_manager.repositories.RequirementImageRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequirementImageService implements RequirementImageRepository {

    private SQLiteHelper sgbd;
    private SQLiteDatabase db;

    private ProjectService projectService;
    private Context context;

    public RequirementImageService(Context context) {
        this.context = context;
        this.projectService = new ProjectService(context);
        this.sgbd = new SQLiteHelper(context, "requirements_manager");
    }


    @Override
    public List<RequirementImage> findAll() {

        RequirementService requirementService = new RequirementService(context);

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "img_url", "requisito_id"};

        Cursor cursor = db.query("tb_requisito_imagem", fields, null, null, null, null, null);

        List<RequirementImage> requirementsImage = new ArrayList<>();

        cursor.moveToFirst();

        for (int i = 1; i <= cursor.getCount(); i++) {

            RequirementImage requirementImage = new RequirementImage();
            requirementImage.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            requirementImage.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));
            //requirementImage.setRequirement(requirementService.findById(cursor.getLong(cursor.getColumnIndexOrThrow("requisito_id"))));

            requirementsImage.add(requirementImage);

            cursor.moveToNext();

        }

        return requirementsImage;

    }

    @Override
    public List<RequirementImage> findByRequirementId(Long id) {

        RequirementService requirementService = new RequirementService(context);

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "img_url", "requisito_id"};

        Cursor cursor = db.query("tb_requisito_imagem", fields, "requisito_id = " + id, null, null, null, null);

        List<RequirementImage> requirementsImage = new ArrayList<>();

        cursor.moveToFirst();

        for (int i = 1; i <= cursor.getCount(); i++) {

            RequirementImage requirementImage = new RequirementImage();
            requirementImage.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            requirementImage.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));
            //requirementImage.setRequirement(requirementService.findById(cursor.getLong(cursor.getColumnIndexOrThrow("requisito_id"))));

            requirementsImage.add(requirementImage);

            cursor.moveToNext();

        }

        return requirementsImage;

    }

    @Override
    public RequirementImage findById(Long id) {

        RequirementService requirementService = new RequirementService(context);

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "img_url", "requisito_id"};

        Cursor cursor = db.query("tb_requisito_imagem", fields, "id = " + id, null, null, null, null);

        if (cursor.moveToFirst()) {

            RequirementImage requirementImage = new RequirementImage();
            requirementImage.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            requirementImage.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));
            //requirementImage.setRequirement(requirementService.findById(cursor.getLong(cursor.getColumnIndexOrThrow("requisito_id"))));

            return requirementImage;

        } else {

            throw  new Resources.NotFoundException("Requisito de Imagem ID " + id + " não encontrado!");

        }

    }

    @Override
    public RequirementImage save(RequirementImage requirementImage) {

        db = sgbd.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("img_url", requirementImage.getUrl());
        values.put("requisito_id", requirementImage.getRequirement().getId());

        long newId = db.insert("tb_requisito_imagem", null, values);

        if (newId != 0) {

            return findById(newId);

        } else {

            throw new SQLException();

        }

    }

    @Override
    public Set<RequirementImage> saveAll(Set<RequirementImage> requirementImages) {

        db = sgbd.getWritableDatabase();

        Set<RequirementImage> requirementImagesInserted = new HashSet<>();

        for (RequirementImage reqImg : requirementImages) {

            ContentValues values = new ContentValues();

            values.put("img_url", reqImg.getUrl());
            values.put("requisito_id", reqImg.getRequirement().getId());

            long newId = db.insert("tb_requisito_imagem", null, values);

            reqImg.setId((int) newId);

            requirementImagesInserted.add(reqImg);

        }

        return requirementImagesInserted;

    }

    @Override
    public RequirementImage update(RequirementImage requirementImage) {
        return null;
    }

    @Override
    public void delete(Long id) {

        db = sgbd.getWritableDatabase();

        long rowsAffected = db.delete("tb_requisito_imagem", "id = " + id, null);

        if (rowsAffected <= 0) {

            throw  new Resources.NotFoundException("Requisito_imagem ID " + id + " not found");

        }

    }

    @Override
    public void deleteAll() {

    }
}
