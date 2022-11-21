package com.senaisantos.bonoup;

import android.content.Context;
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
import static java.lang.Integer.toString;

import androidx.appcompat.app.AppCompatActivity;

public class removeProduto extends AppCompatActivity {

    private Button btnVoltar;
    private ListView listViewProdutos;
    private int idCategoria;

    RemoveProdutosAdapter removeProdutosAdapter;
    List<Produto> listaProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_produto);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);

        listViewProdutos = (ListView) findViewById(R.id.listViewProdutos);

        listaProd = new ArrayList<Produto>();
        removeProdutosAdapter = new RemoveProdutosAdapter(removeProduto.this, listaProd);

        listViewProdutos.setAdapter(removeProdutosAdapter);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent in = getIntent();
            idCategoria = in.getIntExtra("idCategoria", -1);
        }

        listarProdutos();

//        Pedido p = new Pedido();

//        int numMesa = p.getNumMesa();

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(20);

                int idProduto = listaProd.get(i).getId();
                String descProduto = listaProd.get(i).getDescricao();

                itemPedido ipe = new itemPedido();
                ipe.setNomeProduto(descProduto);
                ipe.setIdProduto(idProduto);
                ipe.setQtdProduto(1);

                Intent visualizarRemoveProduto = new Intent(removeProduto.this, visualizarRemoveProduto.class);
                visualizarRemoveProduto.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(visualizarRemoveProduto);
                finish();
            }
        });

    }

    private void listarProdutos(){
        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        String url = ip + "/listarProdutos.php";

        Ion.with(removeProduto.this)
                .load(url)
                .setBodyParameter("idCategoria", Integer.toString(idCategoria))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    @Override
                    public void onCompleted(Exception e, JsonArray result){
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();

                                Produto p = new Produto();
                                p.setId(obj.get("id").getAsInt());
                                p.setDescricao(obj.get("descricao").getAsString());
                                p.setPreco(obj.get("preco").getAsDouble());

                                listaProd.add(p);
                            }

                            removeProdutosAdapter.notifyDataSetChanged();
                        }catch (Exception erro){
                            Toast.makeText(removeProduto.this, erro + "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

}

