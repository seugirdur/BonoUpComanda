package com.senaisantos.bonoup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import androidx.appcompat.app.AppCompatActivity;

public class Configuracao extends AppCompatActivity {

    private Button btnVoltar, btnTestar, btnSalvar;
    private TextView lblTitulo;
    private EditText edtEndereco;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean endvalido = false;
    String url, urlTest, ipSolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracao);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        String ip = prefs.getString("ip", "");

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnTestar = (Button) findViewById(R.id.btnTestar);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);

        if (!ip.isEmpty()) {
            edtEndereco.setText(ip.substring(7, ip.length() - 23));
        }

        btnTestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checarConexao(edtEndereco.getText().toString());
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTheConecction();

            }
        });

    }

    public void reiniciarMesa(View view) {
        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        urlTest = ip + "/reiniciarMesas.php";

        Ion.with(Configuracao.this)
                .load(urlTest)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("status").getAsString();

                            //Toast.makeText(Login.this, RETORNO + ".", Toast.LENGTH_LONG).show();

                            if (RETORNO.equals("erro")) {
                                Toast.makeText(Configuracao.this, "Endereço existente, porém erro ao conectar no banco de dados", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Configuracao.this, "Mesas reiniciadas com sucesso!", Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception erro) {
                            Toast.makeText(Configuracao.this, "Ocorreu um erro! Tente outro endereço.", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    private void doTheConecction() {
        if (endvalido) {
            final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
            String ip = prefs.getString("ip", "");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ip", url);
            editor.commit();

            Intent config = new Intent(Configuracao.this, Login.class);
            startActivity(config);

            //finish();

        } else {
            Toast.makeText(Configuracao.this, "Preencha um endereço válido antes de salvar", Toast.LENGTH_LONG).show();
        }
    }

    private void checarConexao(final String endereco) {
        ipSolo = endereco;
        url = "http://" + endereco + ":8000/comandaeletronica";
        urlTest = url + "/testa_conexao.php";
//        String url =  endereco + "/testa_conexao.php";

        if (endereco.isEmpty()) {
            Toast.makeText(Configuracao.this, "Preencha o endereço", Toast.LENGTH_LONG).show();
        } else {
            Ion.with(Configuracao.this)
                    .load(urlTest)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                String RETORNO = result.get("status").getAsString();

                                //Toast.makeText(Login.this, RETORNO + ".", Toast.LENGTH_LONG).show();

                                if (RETORNO.equals("erro")) {
                                    Toast.makeText(Configuracao.this, "Endereço existente, porém erro ao conectar no banco de dados", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Configuracao.this, "Conexão efetuada com sucesso!", Toast.LENGTH_LONG).show();

                                    endvalido = true;
                                    doTheConecction();
                                }
                            } catch (Exception erro) {
                                Toast.makeText(Configuracao.this, "Ocorreu um erro! Tente outro endereço.", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }
    }


}
