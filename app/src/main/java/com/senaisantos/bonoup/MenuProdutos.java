package com.senaisantos.bonoup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Tela com menu para os produtos
public class MenuProdutos extends AppCompatActivity {

    Button btnAdicionarProdutos, btnRemoverProdutos, btnVoltarMenuProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_produtos);

        btnAdicionarProdutos=findViewById(R.id.btnAdicionarProduto);
        btnRemoverProdutos=findViewById(R.id.btnRemoverProduto);
        btnVoltarMenuProdutos=findViewById(R.id.btnVoltarMenuProdutos);

        btnAdicionarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adicionaProduto = new Intent(MenuProdutos.this, adicionaProduto.class);
                startActivity(adicionaProduto);
            }
        });

        btnRemoverProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent removeCategoria = new Intent(MenuProdutos.this, removeCategoria.class);
                startActivity(removeCategoria);
            }
        });


        btnVoltarMenuProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




}