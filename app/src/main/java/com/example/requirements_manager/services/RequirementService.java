package com.example.requirements_manager.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.requirements_manager.database.SQLiteHelper;
import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.repositories.RequirementRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RequirementService implements RequirementRepository {

    private SQLiteHelper sgbd;
    private SQLiteDatabase db;

    public RequirementService (Context context) {
        this.sgbd = new SQLiteHelper(context, "requirements_manager");
    }

    @Override
    public List<Requirement> findAll() {

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "titulo", "descricao", "prioridade", "dificuldade", "tempo_estimado", "momento_registro", "projeto_id"};

        Cursor cursor = db.query("tb_requisito", fields, null, null, null, null, null );

        List<Requirement> requirements = new ArrayList<>();

        cursor.moveToFirst();

        for (int i = 1; i <= cursor.getCount(); i++) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("momento_registro")), formatter);

            Requirement requirement = new Requirement();
            requirement.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            requirement.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
            requirement.setDesc(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            requirement.setImportance(cursor.getString(cursor.getColumnIndexOrThrow("prioridade")));
            requirement.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow("dificuldade")));
            requirement.setHours(cursor.getInt(cursor.getColumnIndexOrThrow("tempo_estimado")));
            requirement.setMoment(dateTime.atZone(ZoneId.systemDefault()).toInstant());

            //TODO VERIFICAR DATA

            requirements.add(requirement);

        }

        return requirements;

    }

    @Override
    public List<Requirement> findByProjectId(Long id) {

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "titulo", "descricao", "prioridade", "dificuldade", "tempo_estimado", "momento_registro", "projeto_id"};

        Cursor cursor = db.query("tb_requisito", fields, "projeto_id = " + id, null, null, null, null );

        List<Requirement> requirements = new ArrayList<>();

        cursor.moveToFirst();

        for (int i = 1; i <= cursor.getCount(); i++) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("momento_registro")), formatter);

            Requirement requirement = new Requirement();
            requirement.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            requirement.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
            requirement.setDesc(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            requirement.setImportance(cursor.getString(cursor.getColumnIndexOrThrow("prioridade")));
            requirement.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow("dificuldade")));
            requirement.setHours(cursor.getInt(cursor.getColumnIndexOrThrow("tempo_estimado")));
            requirement.setMoment(dateTime.atZone(ZoneId.systemDefault()).toInstant());

            //TODO VERIFICAR DATA

            requirements.add(requirement);

            cursor.moveToNext();

        }

        return requirements;

    }

    @Override
    public Requirement findById(Long id) {

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "titulo", "descricao", "prioridade", "dificuldade", "tempo_estimado", "momento_registro", "projeto_id"};

        Cursor cursor = db.query("tb_requisito", fields, "id = " + id, null, null, null, null );

        if (cursor.moveToFirst()) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            Requirement requirement = new Requirement();
            requirement.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            requirement.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
            requirement.setDesc(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            requirement.setImportance(cursor.getString(cursor.getColumnIndexOrThrow("prioridade")));
            requirement.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow("dificuldade")));
            requirement.setHours(cursor.getInt(cursor.getColumnIndexOrThrow("tempo_estimado")));

            try {
                requirement.setMoment(Instant.parse(cursor.getString(cursor.getColumnIndexOrThrow("momento_registro"))));
            } catch (DateTimeParseException ex) {
                requirement.setMoment((Instant) dtf.parse(cursor.getString(cursor.getColumnIndexOrThrow("momento_registro"))));
            }

            return requirement;

        } else {

            throw new Resources.NotFoundException("Resource ID " + id + " not found!");

        }

    }

    @Override
    public Requirement save(Requirement requirement) {

        db = sgbd.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("titulo", requirement.getTitle());
        values.put("descricao", requirement.getDesc());
        values.put("prioridade", requirement.getImportance());
        values.put("dificuldade", requirement.getDifficulty());
        values.put("tempo_estimado", requirement.getHours());
        values.put("projeto_id", requirement.getProject().getId());

        long newId = db.insert("tb_requisito", null, values);

        if (newId != 0) {

            return  findById(newId);

        } else {

            throw new SQLException();

        }

    }

    @Override
    public List<Requirement> saveAll(List<Requirement> requirements) {

        db = sgbd.getWritableDatabase();

        List<Requirement> requirementsInserted = new ArrayList<>();

        for (Requirement req : requirements) {

            ContentValues values = new ContentValues();

            values.put("titulo", req.getTitle());
            values.put("descricao", req.getDesc());
            values.put("prioridade", req.getImportance());
            values.put("dificuldade", req.getDifficulty());
            values.put("tempo_estimado", req.getHours());
            values.put("projeto_id", req.getProject().getId());

            long newId = db.insert("tb_requisito", null, values);

            req.setId((int) newId);

            requirementsInserted.add(req);

        }

        return requirementsInserted;

    }

    @Override
    public Requirement update(Requirement requirement) {
        return null;
    }

    @Override
    public void delete(Long id) {

        db = sgbd.getWritableDatabase();

        long rows = db.delete("tb_requisito", "id = " + id, null);

        if (rows == 0) {

            throw new Resources.NotFoundException("Requisito ID " + id + " n encontrado!");

        }

    }

    //TODO remove after
    public void deleteAll() {

        db = sgbd.getWritableDatabase();

        long rows = db.delete("tb_requisito", "id > 0", null);

    }
}
