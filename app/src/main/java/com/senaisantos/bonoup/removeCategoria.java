package com.senaisantos.bonoup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import androidx.appcompat.app.AppCompatActivity;

public class removeCategoria extends AppCompatActivity {

    private Button btnVoltar;
    private ListView listViewCategorias;
    private TextView lblTitulo;

    RemoveCategoriasAdapter removeCategoriasAdapter;
    List<Categoria> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_categoria);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);

        listViewCategorias = (ListView) findViewById(R.id.listViewCategorias);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        lista = new ArrayList<Categoria>();
        removeCategoriasAdapter = new RemoveCategoriasAdapter(removeCategoria.this, lista);

        listViewCategorias.setAdapter(removeCategoriasAdapter);
        listarCategorias(ip);
        lblTitulo.setText("Remover Produto");


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        listViewCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(20);

                int idCategoria = lista.get(i).getId();

                Intent removeProduto = new Intent(removeCategoria.this, removeProduto.class);
                removeProduto.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                removeProduto.putExtra("idCategoria", idCategoria);
                startActivity(removeProduto);
            }
        });

    }

    private void listarCategorias(String ip){

        String url = ip + "/listarCategorias.php";

        Ion.with(removeCategoria.this)
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    @Override
                    public void onCompleted(Exception e, JsonArray result){
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();

                                Categoria c = new Categoria();
                                c.setId(obj.get("id").getAsInt());
                                c.setNome(obj.get("nome").getAsString());

                                lista.add(c);
                            }

                            removeCategoriasAdapter.notifyDataSetChanged();
                        }catch (Exception erro){
                            Toast.makeText(removeCategoria.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(removeCategoria.this, R.style.AlertDialogCustom);
        builder1.setMessage("Sair da remoção de produto?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "Não",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
