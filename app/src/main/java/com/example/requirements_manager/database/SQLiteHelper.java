package com.example.requirements_manager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_usuario_v1 = "CREATE TABLE tb_usuario (" +
                                                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                        "    nome TEXT," +
                                                        "    empresa TEXT," +
                                                        "    email TEXT UNIQUE," +
                                                        "    senha TEXT" +
                                                        "); ";

    private static final String CREATE_TABLE_projeto_v1 = "CREATE TABLE tb_projeto (" +
                                                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                        "    nome TEXT," +
                                                        "    data_inicio DATE," +
                                                        "    data_entrega DATE," +
                                                        "    usuario_id INTEGER," +
                                                        "    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)" +
                                                        "); ";

    private static final String CREATE_TABLE_requisito_v1 = "CREATE TABLE tb_requisito (\n" +
                                                        "    id INTEGER PRIMARY KEY,\n" +
                                                        "    titulo TEXT,\n" +
                                                        "    descricao TEXT,\n" +
                                                        "    prioridade INTEGER,\n" +
                                                        "    dificuldade INTEGER,\n" +
                                                        "    tempo_estimado INTEGER,\n" +
                                                        "    momento_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                                                        "    projeto_id INTEGER,\n" +
                                                        "    FOREIGN KEY (projeto_id) REFERENCES tb_projeto(id)\n" +
                                                        "); ";

    public SQLiteHelper(@Nullable Context context, @Nullable String database_name) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_usuario_v1);

        db.execSQL(CREATE_TABLE_projeto_v1);

        db.execSQL(CREATE_TABLE_requisito_v1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
