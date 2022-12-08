package com.senaisantos.bonoup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import androidx.appcompat.app.AppCompatActivity;

//tela de visualizacao de comanda
public class visualizarComanda extends AppCompatActivity {

    Context context;

    private Button btnVoltar, btnEnviarComanda;
    private ListView listViewItens;
    private TextView lblTitulo;
    private LinearLayout bottom;
    String tipoPagamento, idCategoria;
    String valorString;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Double precoFinal;
    ItensAdapter itensAdapter;
    VisualizarItensAdapter visualizarItensAdapter;
    List<itemPedidoLista> lista;
    Dialog mDialog;


    @Override
    public void onResume() {
        super.onResume();

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        lista.clear();
        Pedido p = new Pedido();

        String acao = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent in = getIntent();
            acao = in.getStringExtra("acao");
        }

        if (acao.equalsIgnoreCase("consultarMesa")) {
            bottom = (LinearLayout) findViewById(R.id.bottom);

            btnEnviarComanda.setText(R.string.marcar_concluido);

            visualizarItensAdapter.notifyDataSetChanged();
            listarPedidoMesa(ip, p.getIdMesa(), p.getId());
        } else {
            itensAdapter.notifyDataSetChanged();
            listarItensPedido(ip, p.getId());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_comanda);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        btnEnviarComanda = (Button) findViewById(R.id.btnEnviarComanda);
        listViewItens = (ListView) findViewById(R.id.listViewItens);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        context = this;

        lista = new ArrayList<itemPedidoLista>();

        Pedido p = new Pedido();

        int numMesa = p.getNumMesa();
        int idPedido = p.getId();
        lblTitulo.setText("Mesa " + numMesa);

        String acao = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent in = getIntent();
            acao = in.getStringExtra("acao");
        }

        if (acao.equalsIgnoreCase("consultarMesa")) {
            visualizarItensAdapter = new VisualizarItensAdapter(visualizarComanda.this, lista);
            listViewItens.setAdapter(visualizarItensAdapter);

            lblTitulo.setText("Mesa " + numMesa + " | Pedido " + idPedido);

        } else {
            itensAdapter = new ItensAdapter(visualizarComanda.this, lista);
            listViewItens.setAdapter(itensAdapter);
        }


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEnviarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String acao = "";
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Intent in = getIntent();
                    acao = in.getStringExtra("acao");
                }

                if (acao.equalsIgnoreCase("consultarMesa")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(visualizarComanda.this, R.style.AlertDialogCustom);
                    builder1.setMessage("Deseja realmente concluir esse pedido?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Sim",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    abrirPagamento();

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

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(visualizarComanda.this, R.style.AlertDialogCustom);
                    builder1.setMessage("Deseja realmente enviar a comanda para cozinha?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Sim",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDialog = new Dialog(visualizarComanda.this);

                                    mDialog.setContentView(R.layout.pop_nome_cliente);
                                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    mDialog.show();

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
        });

        listViewItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String acao = "";
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Intent in = getIntent();
                    acao = in.getStringExtra("acao");
                }

                if (acao.equalsIgnoreCase("consultarMesa")) {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(20);
                } else {

                    itemPedido ipe = new itemPedido();

                    int idItemPedido = lista.get(i).getIdItemPedido();
                    int idProduto = lista.get(i).getIdProduto();

                    int qtdProduto = lista.get(i).getQtdProduto();
                    String nomeProduto = lista.get(i).getNomeProduto();

                    ipe.setIdItemPedido(idItemPedido);
                    ipe.setIdProduto(idProduto);
                    ipe.setQtdProduto(qtdProduto);
                    ipe.setNomeProduto(nomeProduto);

                    Intent qtdprod = new Intent(visualizarComanda.this, quantidadeProduto.class);
                    qtdprod.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    qtdprod.putExtra("acao", "alterar");
                    startActivity(qtdprod);
                }
            }
        });

    }

    public void pgtoPix(View view) {
        tipoPagamento = "Pix";

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        concluirVenda(ip);
        concluirPedido(ip);

    }

    public void pgtoDinheiro(View view) {



        tipoPagamento = "Dinheiro";

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        concluirVenda(ip);
        concluirPedido(ip);
    }

    public void calcularTroco(View view) {
        Dialog mDialog = new Dialog(visualizarComanda.this);

        mDialog.setContentView(R.layout.pop_troco);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        TextView valorTotalComanda = (TextView) mDialog.findViewById(R.id.valorTotalComanda);
        EditText etTrocoDado = (EditText) mDialog.findViewById(R.id.etTrocoDado);
        TextView tvTrocoCalculado = (TextView) mDialog.findViewById(R.id.tvTrocoCalculado);
        Button btFinalizarDinheiro = (Button) mDialog.findViewById(R.id.btFinalizarDinheiro);
        Button btCalcularTroco = (Button) mDialog.findViewById(R.id.btCalcularTroco);
        TextView avisoTrocoNegativo = (TextView) mDialog.findViewById(R.id.avisoTrocoNegativo);

        valorTotalComanda.setText(valorString);

        btCalcularTroco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trocoDado = etTrocoDado.getText().toString();

                String trocoDadosemPonto = trocoDado.replaceAll(",", ".");

                Double troco = Double.valueOf(Double.parseDouble(trocoDadosemPonto) - precoFinal);
                DecimalFormat df = new DecimalFormat("#,##0.00");



                tvTrocoCalculado.setText("R$ " + df.format(troco));

                if (troco<0) {
                    avisoTrocoNegativo.setVisibility(View.VISIBLE);

                } else {
                    avisoTrocoNegativo.setVisibility(View.GONE);

                }


            }
        });

    }

    public void pgtoDebito(View view) {
        tipoPagamento = "Débito";

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        concluirVenda(ip);
        concluirPedido(ip);
    }

    public void pgtoCredito(View view) {
        tipoPagamento = "Credito";

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");
        concluirVenda(ip);
        concluirPedido(ip);
    }

    private void abrirPagamento() {

        Dialog mDialog = new Dialog(visualizarComanda.this);

        // Defini o click dentro do popup
        mDialog.setContentView(R.layout.pop_pagamento);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }


    private void concluirVenda(String ip) {

        Pedido p = new Pedido();
        int idPedido = p.getId();

        String cadaNome = "";
        String todosProdutos = "";
        precoFinal = 0.0;
        int size = lista.size();
        int qtddeItens = 0;
        for (int i = 0; i < size; i++) {


            Double precoUnitario = lista.get(i).getPrecoItem();
            int qtdUnitario = lista.get(i).getQtdProduto();

            Double precoMultiplicado = precoUnitario * qtdUnitario;

            precoFinal += precoMultiplicado;

            cadaNome = lista.get(i).getNomeProduto();
            qtddeItens++;



            String url = ip + "/concluirVenda.php";

            Ion.with(visualizarComanda.this)
                    .load(url)
                    .setBodyParameter("idPedido", Integer.toString(idPedido))
                    .setBodyParameter("vendaValor", Double.toString(precoMultiplicado))
                    .setBodyParameter("formaPagamento", tipoPagamento)
                    .setBodyParameter("itensComprados", cadaNome)
                    .setBodyParameter("qtdItens", Integer.toString(qtdUnitario))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                String RETORNO = result.get("status").getAsString();

                                if (RETORNO.equals("erro")) {
                                Toast.makeText(visualizarComanda.this, "lmao Erro ao enviar comanda.", Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception erro) {
                            Toast.makeText(visualizarComanda.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                            }
                        }

                    });


            todosProdutos += cadaNome + "| ";
        }


        String todosProdutosFinal = todosProdutos.substring(0, todosProdutos.length() - 2);

    }

    public void enviarNomeCliente(View view) {

        final SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        final String ip = prefs.getString("ip", "");

        EditText edit = (EditText) mDialog.findViewById(R.id.etNomeCliente);
        String nomeCliente = edit.getText().toString();

        if (nomeCliente.isEmpty()) {
            edit.setText("");
            Toast.makeText(this, "Escreva o nome do cliente", Toast.LENGTH_SHORT).show();
        } else {
            enviarComanda(ip, nomeCliente);
        }

    }

    private void listarItensPedido(String ip, int idPedido) {

        String url = ip + "/listarItensPedido.php";

        Ion.with(visualizarComanda.this)
                .load(url)
                .setBodyParameter("idPedido", Integer.toString(idPedido))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {

                            precoFinal = 0.0;
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();

                                itemPedidoLista ipe = new itemPedidoLista();
                                ipe.setNomeProduto(obj.get("nome").getAsString());
                                String a = ipe.getNomeProduto();
                                ipe.setQtdProduto(obj.get("qtd").getAsInt());
                                String b = String.valueOf(ipe.getQtdProduto());
                                ipe.setPrecoItem(obj.get("preco").getAsDouble());
                                String c = String.valueOf(ipe.getPrecoItem());
                                ipe.setPrecoItemstr(obj.get("preco").getAsString());
                                String d = ipe.getPrecoItemstr();
                                ipe.setIdItemPedido(obj.get("iditem").getAsInt());
                                String f = String.valueOf(ipe.getIdItemPedido());

                                Double precoUnitario = obj.get("preco").getAsDouble();


                                precoFinal += precoUnitario;


                                lista.add(ipe);
                            }

                            Locale ptBr = new Locale("pt", "BR");

                            valorString = NumberFormat.getCurrencyInstance(ptBr).format(precoFinal);

                            TextView tvPrecoFinal = findViewById(R.id.precoTotal);
                            tvPrecoFinal.setEnabled(true);
                            tvPrecoFinal.setText("Preço total: " + valorString);

                            if (itensAdapter.getCount() == 0) {
                                Toast.makeText(visualizarComanda.this, "Nenhum item foi adicionado na comanda.", Toast.LENGTH_LONG).show();
                                btnEnviarComanda.setEnabled(false);
                            }
                            itensAdapter.notifyDataSetChanged();
                        } catch (Exception erro) {
                            Toast.makeText(visualizarComanda.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }


    private void enviarComanda(String ip, String nomeCliente) {

        Pedido p = new Pedido();
        int idMesa = p.getIdMesa();
        int idPedido = p.getId();

        String url = ip + "/enviarComanda.php";

        Ion.with(visualizarComanda.this)
                .load(url)
                .setBodyParameter("idPedido", Integer.toString(idPedido))
                .setBodyParameter("idMesa", Integer.toString(idMesa))
                .setBodyParameter("nomeCliente", nomeCliente)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("status").getAsString();

                            if (RETORNO.equals("erro")) {
                                Toast.makeText(visualizarComanda.this, "Erro ao enviar comanda.", Toast.LENGTH_LONG).show();
                            } else {
                                Intent i = new Intent(visualizarComanda.this, Menu.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                                Toast.makeText(visualizarComanda.this, "Comanda enviada com sucesso!", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception erro) {
                            Toast.makeText(visualizarComanda.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void listarPedidoMesa(String ip, int idMesa, int idPedido) {


        String url = ip + "/listarPedidoMesa.php";

        Ion.with(visualizarComanda.this)
                .load(url)
                .setBodyParameter("idMesa", Integer.toString(idMesa))
                .setBodyParameter("idPedido", Integer.toString(idPedido))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {

                            precoFinal = 0.0;

                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();

                                itemPedidoLista ipe = new itemPedidoLista();
                                ipe.setNomeProduto(obj.get("nome").getAsString());
                                ipe.setQtdProduto(obj.get("qtd").getAsInt());
                                ipe.setIdItemPedido(obj.get("iditem").getAsInt());
                                ipe.setPrecoItem(obj.get("precoProduto").getAsDouble());

                                Double precoUnitario = obj.get("precoProduto").getAsDouble();
                                int qtdUnitario = obj.get("qtd").getAsInt();

                                Double precoMultiplicado = precoUnitario * qtdUnitario;


                                precoFinal += precoMultiplicado;


                                lista.add(ipe);
                            }

                            Locale ptBr = new Locale("pt", "BR");
                             valorString = NumberFormat.getCurrencyInstance(ptBr).format(precoFinal);

                            TextView tvPrecoFinal = findViewById(R.id.precoTotal);
                            tvPrecoFinal.setEnabled(true);
                            tvPrecoFinal.setText("Preço Total: " + valorString);

                            if (visualizarItensAdapter.getCount() == 0) {
                                Toast.makeText(visualizarComanda.this, "Nenhum item foi encontrado.", Toast.LENGTH_LONG).show();
                            }
                            visualizarItensAdapter.notifyDataSetChanged();
                        } catch (Exception erro) {
                            Toast.makeText(visualizarComanda.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }


    private void concluirPedido(String ip) {

        Pedido p = new Pedido();
        int idPedido = p.getId();


        String url = ip + "/concluirPedido.php";

        Ion.with(visualizarComanda.this)
                .load(url)
                .setBodyParameter("idPedido", Integer.toString(idPedido))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String RETORNO = result.get("status").getAsString();

                            if (RETORNO.equals("erro")) {
                                Toast.makeText(visualizarComanda.this, "Erro ao concluir pedido.", Toast.LENGTH_LONG).show();
                            } else {
                                Intent i = new Intent(visualizarComanda.this, Menu.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                                Toast.makeText(visualizarComanda.this, "Pedido concluído com sucesso!", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception erro) {
                            Toast.makeText(visualizarComanda.this, "Ocorreu um erro! Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }


}
