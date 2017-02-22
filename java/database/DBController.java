package com.example.erick.rrq.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

/**
 * Created by erick on 19/02/17.
 * Classe de acesso e controle do Banco de Dados SQLite.
 */

public class DBController {

    /* Atributos da classe */
    private SQLiteDatabase db;
    private final String table = "grupo";
    private DBCreator dbCreator;
    private ArrayAdapter<String> adpContent;

    /* Construtor da classe */
    public DBController(Context context){
        dbCreator = new DBCreator(context, "DBNomes", null, 1);
        db = dbCreator.getWritableDatabase();
    }


    /*Método que insere conteúdo no banco de dados */
    public void saveContent(String nome){
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        db.insert(table, null, values);
    }

    /*Método removedor de conteúdo do banco de dados */
    public void removeContent(String nome){
        db.delete(table, "nome=?", new String[]{nome});
    }

    /* Método conector do banco de dados com adaptador para ListView */
    public ArrayAdapter<String> showContent(Context context) {
        adpContent = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1);
        Cursor cursor;
        cursor = db.query(table, null, null, null, null, null, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                String nome = cursor.getString(1);
                adpContent.add(nome);
            }while (cursor.moveToNext());
        }
        return adpContent;
    }

    /* Método limpador de tabela*/
    public void clearData(){
        db.delete(table, null, null);
    }

    public long getDataCount(){
        long size =  db.getPageSize();
        return size;
    }
}
