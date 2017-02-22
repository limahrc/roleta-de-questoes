package com.example.erick.rrq.tabs;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.erick.rrq.R;
import com.example.erick.rrq.database.DBController;

import java.util.ArrayList;
import java.util.Random;

public class TabSortear extends Fragment {

    Switch removeSorteado, removeDB;
    Button botaoSortear, botaoReiniciar;
    EditText campoNumero;
    ListView lvSorteados;

    DBController dbController;
    ArrayAdapter<String> adpRegistrados, adpSorteados;
    ArrayList<Integer> outQuests;
    Random random;
    int numQuestions, numPartners, sorteadoPosition, numSort, count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_sortear, container, false);

        removeDB = (Switch) rootView.findViewById(R.id.removeDB);
        removeSorteado = (Switch) rootView.findViewById(R.id.removeSorteado);
        botaoSortear = (Button) rootView.findViewById(R.id.botaoSortear);
        botaoReiniciar = (Button) rootView.findViewById(R.id.botaoReiniciar);
        campoNumero = (EditText) rootView.findViewById(R.id.campoNumero);
        lvSorteados = (ListView) rootView.findViewById(R.id.lvSorteados);

        dbController = new DBController(getContext());
        adpRegistrados = dbController.showContent(getContext());
        adpSorteados = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        random = new Random();
        lvSorteados.setAdapter(adpSorteados);

        botaoSortear.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                actionBotaoSortear();
            }
        });

        botaoReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                removeSorteado.setChecked(false);
                removeDB.setChecked(false);
                campoNumero.setText(null);
                adpSorteados.clear();
            }
        });

        outQuests = new ArrayList<>();

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void actionBotaoSortear() {
        if (campoNumero.getText().length() > 0) {
            numPartners = adpRegistrados.getCount();
            if (numPartners > 0) {
                numQuestions = Integer.parseInt(campoNumero.getText().toString());
                if (count < numQuestions) {
                        numSort = 1 + random.nextInt(numQuestions);
                        sorteadoPosition = random.nextInt(numPartners);
                        count++;

                        while (outQuests.contains(numSort)) {
                            numSort = 1 + random.nextInt(numQuestions);
                        }

                        int questao = numSort;
                        outQuests.add(questao);

                        final String sorteado = adpRegistrados.getItem(sorteadoPosition);

                        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                        ad.setTitle("E o felizardo é...");
                        ad.setMessage(Html.fromHtml("\n\n" + "<b>" + sorteado + "</b>" +
                                ", que fará a " + "<b>" + questao + "ª" + "</b>" + " questão!"));
                        ad.setPositiveButton("Ok", null);
                        ad.show();
                        adpSorteados.add(sorteado + " fez a " + questao + "ª questão");

                        if (removeSorteado.isChecked()) {
                            adpRegistrados.remove(sorteado);
                        }
                        if (removeDB.isChecked()) {
                            removeSorteado.setChecked(true);
                            adpRegistrados.remove(sorteado);
                            dbController.removeContent(sorteado);
                        }
                    } else Toast.makeText(getContext(), "Todas as questões já foram sorteadas",
                        Toast.LENGTH_SHORT).show();

                } else Toast.makeText(getContext(), "Todos os participantes já foram sorteados",
                        Toast.LENGTH_LONG).show();

        } else Toast.makeText(getContext(), "Informe o número de questões",
                    Toast.LENGTH_SHORT).show();
    }
}
