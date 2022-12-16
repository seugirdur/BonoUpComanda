package com.senaisantos.bonoup;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.content.Context;
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
import androidx.appcompat.widget.SearchView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

//tela de visualização de produtos
public class alterarProduto extends AppCompatActivity {

    private Button btnVoltar, btnVisualizarComanda;
    private ListView listViewProdutos;
    private TextView lblTitulo;
    private int idCategoria;
    private SearchView searchView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    AlterarProdutosAdapter alterarProdutosAdapter;
    List<Produto> listaProd;
    List<Produto> filteredList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_produto);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnVisualizarComanda = (Button) findViewById(R.id.btnVisualizarComanda);
        searchView = findViewById(R.id.searchprodutos);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                alterarProdutosAdapter.

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                prod
                filterList(newText);

                return true;
            }
        });


        listViewProdutos = (ListView) findViewById(R.id.listViewProdutos);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        listaProd = new ArrayList<Produto>();
        alterarProdutosAdapter = new AlterarProdutosAdapter(alterarProduto.this, listaProd);

        listViewProdutos.setAdapter(alterarProdutosAdapter);



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent in = getIntent();
            idCategoria = in.getIntExtra("idCategoria", -1);
        }

        listarProdutos();

        Pedido p = new Pedido();

        int numMesa = p.getNumMesa();
        lblTitulo.setText("Mesa "+numMesa);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnVisualizarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vcomanda = new Intent(alterarProduto.this, visualizarComanda.class);
                vcomanda.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                vcomanda.putExtra("acao", "alterarMesa");
                startActivity(vcomanda);
            }
        });

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(20);

//                int idProduto = listaProd.get(i).getId();
//                String descProduto = listaProd.get(i).getDescricao();
//                Double precoProduto = listaProd.get(i).getPreco();

                int idProduto;
                String descProduto;
                Double precoProduto;

                if (filteredList == null) {
                    idProduto = listaProd.get(i).getId();
                    descProduto = listaProd.get(i).getDescricao();
                    precoProduto = listaProd.get(i).getPreco();
                } else {
                    idProduto = filteredList.get(i).getId();
                    descProduto = filteredList.get(i).getDescricao();
                    precoProduto = filteredList.get(i).getPreco();
                }

                itemPedido ipe = new itemPedido();
                ipe.setNomeProduto(descProduto);
                ipe.setIdProduto(idProduto);
                ipe.setQtdProduto(1);
                ipe.setPrecoItem(precoProduto);

                Intent qtdProduto = new Intent(alterarProduto.this, alterarQuantidadeProduto.class);
                qtdProduto.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(qtdProduto);
            }
        });

    }

    private void filterList(String text) {
        filteredList = new ArrayList<>();
        for (Produto produto : listaProd) {
            if (produto.getDescricao().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(produto);
            }
        }
//
//        if (filteredList.isEmpty()) {
//            Toast.makeText(mostrarProduto.this, "Item não encontrado", Toast.LENGTH_SHORT).show();
//        } else {
//            AlterarProdutosAdapter.setFilteredList(filteredList);
//        }

        if (filteredList != null) {
            if (filteredList.isEmpty()) {
                alterarProdutosAdapter.setFilteredList(filteredList);
            } else {
                alterarProdutosAdapter.setFilteredList(filteredList);
                AlterarProdutosAdapter.setFilteredList(filteredList);

            }
        }
    }

    private void listarProdutos(){
        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        String url = ip + "/listarProdutos.php";

        Ion.with(alterarProduto.this)
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

                            alterarProdutosAdapter.notifyDataSetChanged();
                        }catch (Exception erro){
                            Toast.makeText(alterarProduto.this, erro + "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

}

