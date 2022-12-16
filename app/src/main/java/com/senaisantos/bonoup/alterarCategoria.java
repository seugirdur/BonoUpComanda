package com.senaisantos.bonoup;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class alterarCategoria extends AppCompatActivity {

    private Button btnVoltar, btnVisualizarComanda;
    private ListView listViewCategorias;
    private TextView lblTitulo;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    AlterarCategoriasAdapter alterarCategoriasAdapter;
    List<Categoria> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_categoria);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnVisualizarComanda = (Button) findViewById(R.id.btnVisualizarComanda);

        listViewCategorias = (ListView) findViewById(R.id.listViewCategorias);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
//        Toast.makeText(this, "teste alterar", Toast.LENGTH_SHORT).show();

        lista = new ArrayList<Categoria>();
        alterarCategoriasAdapter = new AlterarCategoriasAdapter(alterarCategoria.this, lista);

        listViewCategorias.setAdapter(alterarCategoriasAdapter);

        listarCategorias(ip);

        Pedido p = new Pedido();

        int numMesa = p.getNumMesa();
        lblTitulo.setText("Mesa "+numMesa);


        btnVisualizarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vcomanda = new Intent(alterarCategoria.this, visualizarComanda.class);
                vcomanda.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                vcomanda.putExtra("acao", "alterarMesa");
                startActivity(vcomanda);
            }
        });

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

                Intent alterarProduto = new Intent(alterarCategoria.this, alterarProduto.class);
                alterarProduto.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                alterarProduto.putExtra("idCategoria", idCategoria);
                startActivity(alterarProduto);
            }
        });

    }

    private void listarCategorias(String ip){

        String url = ip + "/listarCategorias.php";

        Ion.with(alterarCategoria.this)
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

                            alterarCategoriasAdapter.notifyDataSetChanged();
                        }catch (Exception erro){
                            Toast.makeText(alterarCategoria.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void cancelarPedido(){
        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        String url = ip + "/cancelarPedido.php";

        Pedido p = new Pedido();
        int idMesa = p.getIdMesa();
        int idPedido = p.getId();

        Ion.with(alterarCategoria.this)
                .load(url)
                .setBodyParameter("idMesa", Integer.toString(idMesa))
                .setBodyParameter("idPedido", Integer.toString(idPedido))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>(){
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("status").getAsString();

                            if (RETORNO.equals("erro")) {
                                Toast.makeText(alterarCategoria.this, "Erro ao cancelar o pedido.", Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(alterarCategoria.this, "Pedido cancelado", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(alterarCategoria.this, selecionarMesa.class);
                                i.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        } catch (Exception erro) {
                            Toast.makeText(alterarCategoria.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    @Override
    public void onBackPressed() {
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(alterarCategoria.this, R.style.AlertDialogCustom);
//        builder1.setMessage("Deseja realmente cancelar o pedido?");
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton(
//                "Sim",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        cancelarPedido();
//
//                    }
//                });
//
//        builder1.setNegativeButton(
//                "Não",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alert11 = builder1.create();
//        alert11.show();

        finish();

    }

}
