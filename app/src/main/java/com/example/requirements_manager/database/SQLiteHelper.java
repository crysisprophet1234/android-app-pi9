package com.example.requirements_manager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_usuario_v1 = "CREATE TABLE tb_usuario (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT," +
            "empresa TEXT," +
            "email TEXT UNIQUE," +
            "senha TEXT" +
            "); ";

    private static final String CREATE_TABLE_projeto_v1 = "CREATE TABLE tb_projeto (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT," +
            "data_inicio DATE," +
            "data_entrega DATE," +
            "usuario_id INTEGER," +
            "FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)" +
            "); ";

    private static final String CREATE_TABLE_requisito_v1 = "CREATE TABLE tb_requisito (" +
            "id INTEGER PRIMARY KEY," +
            "titulo TEXT," +
            "descricao TEXT," +
            "prioridade INTEGER," +
            "dificuldade INTEGER," +
            "tempo_estimado INTEGER," +
            "momento_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "projeto_id INTEGER," +
            "FOREIGN KEY (projeto_id) REFERENCES tb_projeto(id)" +
            "); ";

    private static final String CREATE_TABLE_requisito_v2 = "CREATE TABLE tb_requisito (" +
            "id INTEGER PRIMARY KEY," +
            "titulo TEXT," +
            "descricao TEXT," +
            "prioridade INTEGER," +
            "dificuldade INTEGER," +
            "tempo_estimado INTEGER," +
            "momento_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "projeto_id INTEGER," +
            "longitude_registro REAL," +
            "latitude_registro REAL," +
            "FOREIGN KEY (projeto_id) REFERENCES tb_projeto(id)" +
            "); ";

    private static final String CREATE_TABLE_requisito_imagem_v1 = "CREATE TABLE tb_requisito_imagem (" +
            "id INTEGER PRIMARY KEY," +
            "img_url TEXT," +
            "requisito_id INTEGER," +
            "FOREIGN KEY (requisito_id) REFERENCES tb_requisito(id)" +
            "); ";

    public SQLiteHelper(@Nullable Context context, @Nullable String database_name) {
        super(context, database_name, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_usuario_v1);
        db.execSQL(CREATE_TABLE_projeto_v1);
        db.execSQL(CREATE_TABLE_requisito_v1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE tb_requisito ADD COLUMN latitude_registro REAL");
            db.execSQL("ALTER TABLE tb_requisito ADD COLUMN longitude_registro REAL");
            db.execSQL(CREATE_TABLE_requisito_imagem_v1);
        }

        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE tb_projeto ADD COLUMN documentacao_url REAL");
        }

    }
}

