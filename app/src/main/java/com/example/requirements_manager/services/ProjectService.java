package com.example.requirements_manager.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.requirements_manager.database.SQLiteHelper;
import com.example.requirements_manager.entities.Project;
import com.example.requirements_manager.repositories.ProjectRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProjectService implements ProjectRepository {

    private UserService userService;

    private SQLiteHelper sgbd;
    private SQLiteDatabase db;

    public ProjectService (Context context) {
        this.sgbd = new SQLiteHelper(context, "requirements_manager");
    }

    @Override
    public List<Project> findAll() {

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "nome", "data_inicio", "data_entrega", "usuario_id"};

        Cursor cursor = db.query("tb_projeto", fields, null, null, null, null, null );

        List<Project> projects = new ArrayList<>();

        cursor.moveToFirst();

        for (int i=1; i <= cursor.getCount(); i++)

        {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String data_inicio = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"));
            String data_entrega = cursor.getString(cursor.getColumnIndexOrThrow("data_entrega"));
            Integer usuario_id = cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id"));

            Project project = new Project();
            project.setId(id);
            project.setName(nome);
            project.setStartDate(LocalDate.parse(data_inicio, dtf));
            project.setFinalDate(LocalDate.parse(data_entrega, dtf));

            //TODO
            //project.setUser(userService.findById(usuario_id));

            projects.add(project);

            cursor.moveToNext();
        }

        return projects;

    }

    @Override
    public Project findById(Integer id) {

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "nome", "data_inicio", "data_entrega", "usuario_id"};

        Cursor cursor = db.query("tb_projeto", fields, "id = " + id, null, null, null, null );

        if (cursor.moveToFirst()) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String data_inicio = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"));
            String data_entrega = cursor.getString(cursor.getColumnIndexOrThrow("data_entrega"));
            Integer usuario_id = cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id"));

            System.out.println(usuario_id);

            Project project = new Project();
            project.setId((int) id.longValue());
            project.setName(nome);
            project.setStartDate(LocalDate.parse(data_inicio, dtf));
            project.setFinalDate(LocalDate.parse(data_entrega, dtf));

            //TODO
            //project.setUser(userService.findById(usuario_id));

            return project;

        } else {

            throw new Resources.NotFoundException("Resource ID " + id + " not found!");

        }

    }

    @Override
    public Project save(Project project) {

        db = sgbd.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", project.getName());
        values.put("data_inicio", project.getStartDate().toString());
        values.put("data_entrega", project.getFinalDate().toString());
        values.put("usuario_id", project.getUser().getId());

        long newId = db.insert("tb_projeto", null, values);

        if (newId != 0) {

            project.setId(Integer.parseInt(String.valueOf(newId)));
            return project;

        } else {

            throw new SQLException();

        }

    }

    @Override
    public Project update(Project project) {
        return null;
    }

    @Override
    public void delete(Long id) {

        db = sgbd.getWritableDatabase();

        long rows = db.delete("tb_projeto", "id = " + id, null);

        if (rows == 0) {

            throw new Resources.NotFoundException("Projeto ID " + id + " n encontrado!");

        }

    }

    //TODO remove after
    public void deleteAll() {

        db = sgbd.getWritableDatabase();

        long rows = db.delete("tb_projeto", "id > 0", null);

    }

}
