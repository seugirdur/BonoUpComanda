package com.senaisantos.bonoup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class adicionaProduto extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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


        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        btnCadastrarProduto=findViewById(R.id.btnCadastrarProduto);

        spinnerCategoria = findViewById(R.id.spinnerCategoria);

        spinnerCategoria.setOnItemSelectedListener(this);

        String[] Categorias = getResources().getStringArray(R.array.categorias);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_custom_item, Categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);



        btnCadastrarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDescricaoProduto=findViewById(R.id.etDescricaoProduto);
                etPrecoProduto=findViewById(R.id.etPrecoProduto);
                String descricaoProduto = etDescricaoProduto.getText().toString();
                String precoProdutoSemFormato = etPrecoProduto.getText().toString();
                String precoProduto = precoProdutoSemFormato.replaceAll(",", ".");


                if (categoriaProdutoNome.equals("Lanche")) {
                    idCategoria = "1";
                }

                if (categoriaProdutoNome.equals("Bebida")) {
                    idCategoria = "2";
                }

                if (categoriaProdutoNome.equals("Acompanhamento")) {
                    idCategoria = "3";
                }

                if (categoriaProdutoNome.equals("Sobremesa")) {
                    idCategoria = "4";
                }

                adicionarUmProduto(ip, descricaoProduto, precoProduto, idCategoria );

            }
        });

        btnVoltar=findViewById(R.id.btnVoltarAdc);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

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
//                                Intent i = new Intent(adicionaProduto.this, MenuProdutos.class);
//                                startActivity(i);
                                finish();
                            }

                        }catch (Exception erro){
                            Toast.makeText(adicionaProduto.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerCategoria) {
            categoriaProdutoNome = adapterView.getItemAtPosition(i).toString();
//            Toast.makeText(this, newcategoriaProduto, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}