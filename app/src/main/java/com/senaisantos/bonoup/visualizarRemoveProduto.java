package com.senaisantos.bonoup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class visualizarRemoveProduto extends AppCompatActivity {

    private Button btnVoltar, cancelaRemover, confirmaRemover;
    private TextView lblProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_remove_produto);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        confirmaRemover = (Button) findViewById(R.id.confirmaRemover);
        cancelaRemover = (Button) findViewById(R.id.cancelaRemover);
        lblProduto = (TextView) findViewById(R.id.lblProduto);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");


        itemPedido ipe = new itemPedido();
        lblProduto.setText(ipe.getNomeProduto());
        String idProduto = String.valueOf(ipe.getIdProduto());

        Toast.makeText(this, idProduto, Toast.LENGTH_SHORT).show();

        cancelaRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmaRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removerOProduto(ip, idProduto);
            }
        });



        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void removerOProduto(String ip, String idProduto){

        String url = ip + "/removerProduto.php";

        Ion.with(visualizarRemoveProduto.this)
                .load(url)
                .setBodyParameter("idProduto", idProduto)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>(){
                    @Override
                    public void onCompleted(Exception e, JsonObject result){
                        try {
                            if(result.get("status").getAsString().equals("sucesso")) {
                                Toast.makeText(visualizarRemoveProduto.this, "Produto removido com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent backCategoria = new Intent(visualizarRemoveProduto.this, removeCategoria.class);
                                startActivity(backCategoria);
                                finish();
                            }
                        }catch (Exception erro){
                            Toast.makeText(visualizarRemoveProduto.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
