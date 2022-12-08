package com.senaisantos.bonoup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button btnSair, btnFazerPedido, btnConsultarMesa, btnMenuProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        btnSair = (Button) findViewById(R.id.btnSair);
        btnFazerPedido = (Button) findViewById(R.id.btnFazerPedido);
        btnConsultarMesa = (Button) findViewById(R.id.btnConsultarMesa);
        btnMenuProdutos = (Button) findViewById(R.id.btnMenuProdutos);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        String cargo = prefs.getString("cargoUsuario", "");

        if(cargo.equalsIgnoreCase("atendente")) {
            btnConsultarMesa.setVisibility(View.GONE);
            btnMenuProdutos.setVisibility(View.GONE);
        }
        btnFazerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarMesa("fazerPedido");
            }
        });

        btnConsultarMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarMesa("consultarMesa");
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("logado", false);
                editor.commit();

                Intent login = new Intent(Menu.this, Login.class);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
        });

        btnMenuProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MenuProdutos = new Intent(Menu.this, MenuProdutos.class);
                startActivity(MenuProdutos);
            }
        });



    }
    public void selecionarMesa(String acao){
        Intent selecionaMesa = new Intent(Menu.this, selecionarMesa.class);
        selecionaMesa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        selecionaMesa.putExtra("acao", acao);
        startActivity(selecionaMesa);

    }
}
