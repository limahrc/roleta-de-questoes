package com.example.erick.rrq.tabs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erick.rrq.R;
import com.example.erick.rrq.database.DBController;

public class TabRegistrados extends Fragment {

    Button botaoZerar, botaoAtualizar;
    TextView txtRegistro;
    ListView lvRegistrados;

    DBController dbController;
    ArrayAdapter<String> adpNomes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_registrados, container, false);

        /* Recuperando elementos da interface XML */
        botaoZerar = (Button) rootView.findViewById(R.id.botaoZerar);
        botaoAtualizar = (Button) rootView.findViewById(R.id.botaoAtualizar);
        txtRegistro = (TextView) rootView.findViewById(R.id.txtRegistro);
        lvRegistrados = (ListView) rootView.findViewById(R.id.lvRegistrados);

        /* Configurando conexão com o Banco de Dados */
        dbController = new DBController(getContext());
        adpNomes = dbController.showContent(getContext());
        lvRegistrados.setAdapter(adpNomes);

        /* Ajustando janela para não-exibição do teclado */
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lvRegistrados.getWindowToken(), 0);

        setTxtRegistro();

        /* Ações de botão */
        lvRegistrados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder msg = new AlertDialog.Builder(getContext());
                msg.setMessage("Deseja remover "+adpNomes.getItem(position)+" da lista?");
                msg.setTitle("Remover participante");
                msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbController.removeContent(adpNomes.getItem(position));
                        adpNomes.remove(adpNomes.getItem(position));
                    }
                });
                msg.setNegativeButton("Não", null);
                msg.show();
            }
        });

        botaoZerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adpNomes.getCount()>0) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(getContext());
                    msg.setMessage("Tem certeza que deseja apagar " +
                            "todos os registros do banco de dados?");
                    msg.setTitle("Limpar registros");
                    msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbController.clearData();
                            adpNomes.clear();
                            setTxtRegistro();

                        }
                    });
                    msg.setNegativeButton("Não", null);
                    msg.show();
                } else Toast.makeText(getContext(), "O banco de dados já está vazio",
                        Toast.LENGTH_SHORT).show();
            }
        });

        botaoAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adpNomes = dbController.showContent(getContext());
                lvRegistrados.setAdapter(adpNomes);
                setTxtRegistro();
            }
        });

        return rootView;
    }

    /* Verifica quantidade de registros no Banco de Dados e exibe mensagem de vazio */
    public void setTxtRegistro(){
        if (adpNomes.getCount() > 0) txtRegistro.setVisibility(View.INVISIBLE);
        else txtRegistro.setVisibility(View.VISIBLE);
    }
}
