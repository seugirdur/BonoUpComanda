package com.senaisantos.bonoup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VisualizarItensAdapter extends BaseAdapter {

    private Context ctx;
    private List<itemPedidoLista> lista;

    public VisualizarItensAdapter(visualizarComanda ctx2, List<itemPedidoLista> lista2){
        ctx = ctx2;
        lista = lista2;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public itemPedidoLista getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = null;

        if(view == null){
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            v = inflater.inflate(R.layout.item_lista_visualizar_pedido, null);
        }else{
            v = view;
        }

        itemPedidoLista ipe = getItem(position);
        TextView itemDescricao = (TextView) v.findViewById(R.id.itemDescricao);
        TextView itemQtd = (TextView) v.findViewById(R.id.itemQtd);
        TextView itemQtd2 = (TextView) v.findViewById(R.id.itemQtd2);

        Locale ptBr = new Locale("pt", "BR");

        itemDescricao.setText(ipe.getNomeProduto());
        itemQtd.setText("Quantidade: " + Integer.toString(ipe.getQtdProduto()));
        Double lmao = ipe.getPrecoItem();

        Double precoCheio = lmao * ipe.getQtdProduto();
//        String valorString = NumberFormat.getCurrencyInstance(ptBr).format(ipe.getPrecoItem());

        itemQtd2.setText("Preço: " + Double.toString(precoCheio));

        return v;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
