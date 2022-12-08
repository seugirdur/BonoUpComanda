package com.senaisantos.bonoup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

//tela de adicao de produtos
public class adicionaProduto extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //criacao das variaveis
    Button btnCadastrarProduto, btnVoltar;
    EditText etDescricaoProduto, etPrecoProduto;
    Spinner spinnerCategoria;
    String categoriaProdutoNome, idCategoria;

    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_produto);

        //busca do ip guardado
        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        btnCadastrarProduto=findViewById(R.id.btnCadastrarProduto);

        //criacao do spinner para selecao de categorias
        spinnerCategoria = findViewById(R.id.spinnerCategoria);

        spinnerCategoria.setOnItemSelectedListener(this);

        String[] Categorias = getResources().getStringArray(R.array.categorias);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_custom_item, Categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        //evento de clique do botão para adicionar novo produto
        btnCadastrarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDescricaoProduto=findViewById(R.id.etDescricaoProduto);
                etPrecoProduto=findViewById(R.id.etPrecoProduto);
                String descricaoProduto = etDescricaoProduto.getText().toString();
                String precoProdutoSemFormato = etPrecoProduto.getText().toString();
                String precoProduto = precoProdutoSemFormato.replaceAll(",", ".");

                if (categoriaProdutoNome.equals("Adicionais")) {
                    idCategoria = "1";
                }

                if (categoriaProdutoNome.equals("Bebidas")) {
                    idCategoria = "2";
                }

                if (categoriaProdutoNome.equals("Caixa")) {
                    idCategoria = "3";
                }

                if (categoriaProdutoNome.equals("Doces")) {
                    idCategoria = "4";
                }
                if (categoriaProdutoNome.equals("Lanches e Porçoes")) {
                    idCategoria = "5";
                }
                if (categoriaProdutoNome.equals("Padaria e Afins")) {
                    idCategoria = "6";
                }
                if (categoriaProdutoNome.equals("Pastel")) {
                    idCategoria = "7";
                }
                if (categoriaProdutoNome.equals("Promoçoes")) {
                    idCategoria = "8";
                }
                if (categoriaProdutoNome.equals("Salgados Fritos")) {
                    idCategoria = "9";
                }
                if (categoriaProdutoNome.equals("Salgados Assados")) {
                    idCategoria = "10";
                }
                if (categoriaProdutoNome.equals("Sorvetes")) {
                    idCategoria = "11";
                }

                adicionarUmProduto(ip, descricaoProduto, precoProduto, idCategoria );

            }
        });

        //evento de clique de voltar
        btnVoltar=findViewById(R.id.btnVoltarAdc);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    //chamada na api para guardar o produto criado
    private void adicionarUmProduto(String ip, String descricaoProduto, String precoProduto, String idCategoria){

        String url = ip + "/enviarProduto.php";

        Ion.with(adicionaProduto.this)
                .load(url)
                .setBodyParameter("descricaoProduto", descricaoProduto)
                .setBodyParameter("precoProduto", precoProduto)
                .setBodyParameter("idCategoria", idCategoria)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>(){
                    @Override
                    public void onCompleted(Exception e, JsonObject result){
                        try {

                            if(result.get("status").getAsString().equals("sucesso")) {
                                Toast.makeText(adicionaProduto.this, "Produto adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }catch (Exception erro){
                            Toast.makeText(adicionaProduto.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    //leitura do item do spinner para ser guardado na api
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerCategoria) {
            categoriaProdutoNome = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}