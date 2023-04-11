package com.example.requirements_manager.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.requirements_manager.database.SQLiteHelper;
import com.example.requirements_manager.entities.User;
import com.example.requirements_manager.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService implements UserRepository {

    private final SQLiteHelper sgbd;
    private SQLiteDatabase db;

    private final Context context;

    public UserService (Context context) {
        this.context = context;
        this.sgbd = new SQLiteHelper(context, "requirements_manager");
    }

    @Override
    public User loginWithUsernameAndPassword(User user) {

        db = sgbd.getReadableDatabase();

        String[] projection = { "id", "nome", "empresa", "email", "senha" };

        String selection = "nome = ? AND senha = ?";

        String[] selectionArgs = { user.getName(), user.getPassword() };

        Cursor cursor = db.query(
                "tb_usuario",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        User loginUser = null;

        if (cursor.moveToFirst()) {
            loginUser = new User();
            loginUser.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            loginUser.setName(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            loginUser.setCompany(cursor.getString(cursor.getColumnIndexOrThrow("empresa")));
            loginUser.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            loginUser.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
        }

        cursor.close();

        return loginUser;

    }


    @Override
    public List<User> findAll() {

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "nome", "empresa", "email", "senha"};

        Cursor cursor = db.query("tb_usuario", fields, null, null, null, null, null );

        List<User> users = new ArrayList<>();

        cursor.moveToFirst();

        for (int i=1; i <= cursor.getCount(); i++)

        {

            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setCompany(cursor.getString(cursor.getColumnIndexOrThrow("empresa")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("senha")));

            users.add(user);

            cursor.moveToNext();
        }

        return users;

    }

    @Override
    public User findById(Integer id) {

        db = sgbd.getReadableDatabase();

        String[] fields = {"id", "nome", "empresa", "email", "senha"};

        Cursor cursor = db.query("tb_usuario", fields, "id = " + id, null, null, null, null );

        if (cursor.moveToFirst()) {

            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setCompany(cursor.getString(cursor.getColumnIndexOrThrow("empresa")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("senha")));

            return user;

        } else {

            throw new Resources.NotFoundException("Resource ID " + id + " not found!");

        }

    }

    @Override
    public User save(User user) {

        db = sgbd.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id", user.getId() != null ? user.getId() : null);
        values.put("nome", user.getName());
        values.put("empresa", user.getCompany());
        values.put("email", user.getEmail());
        values.put("senha", user.getPassword());

        long result = db.insert("tb_usuario", null, values);

        if (result != 0) {

            return findById(Integer.parseInt(String.valueOf(result)));

        } else {

            throw new SQLException();

        }

    }

    @Override
    public List<User> saveAll(List<User> user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(Integer id) {

        db = sgbd.getWritableDatabase();

        long rows = db.delete("tb_usuario", "id = " + id, null);

        if (rows == 0) {

            throw new Resources.NotFoundException("Usuario ID " + id + " n encontrado!");

        }

    }
}
