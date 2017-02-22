package com.example.erick.rrq.tabs;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.erick.rrq.R;
import com.example.erick.rrq.database.DBController;
import com.example.erick.rrq.database.DBCreator;

public class TabRegistrar extends Fragment {

    EditText campoNome;
    Button botaoRegistrar, botaoLimpar;

    DBCreator dbCreator;
    DBController dbController;
    SQLiteDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_registrar, container, false);

        dbController = new DBController(getContext());

        campoNome = (EditText) rootView.findViewById(R.id.campoNome);
        botaoRegistrar = (Button) rootView.findViewById(R.id.botaoRegistrar);
        botaoLimpar = (Button) rootView.findViewById(R.id.botaoLimpar);

        try{
            dbCreator = new DBCreator(getContext(), "DBNomes", null, 1);
            db = dbCreator.getWritableDatabase();
        } catch (SQLException ex){
            AlertDialog.Builder error = new AlertDialog.Builder(getContext());
            error.setTitle("Erro");
            error.setMessage("Falha ao criar banco de dados </3" +
                    "\nCódigo do erro: "+ex);
            error.show();
        }

        botaoLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(campoNome.getText().length()>0) {
                    campoNome.setText(null);
                } else Toast.makeText(getContext(), "Nada a limpar", Toast.LENGTH_SHORT).show();
            }
        });


        botaoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBotaoRegistrar();
                campoNome.setText(null);
            }
        });


        return rootView;
    }

    public void actionBotaoRegistrar(){
        if(campoNome.getText().length()>0){
            String nome = campoNome.getText().toString();
            dbController.saveContent(nome);
            Toast.makeText(getContext(), "Participante registrado com sucesso!",
                    Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getContext(), "O campo do nome não deve ficar vazio",
                Toast.LENGTH_SHORT).show();
    }
}
