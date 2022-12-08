package com.senaisantos.bonoup;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//classe ferramenta para criacao de lista de categorias
public class CategoriasAdapter extends BaseAdapter {

    private Context ctx;
    private List<Categoria> lista;

    public CategoriasAdapter(mostrarCategoria ctx2, List<Categoria> lista2){
        ctx = ctx2;
        lista = lista2;
    }

    //retorna o tamanho da lista para ser renderizada
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Categoria getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = null;

        //pega o design da lista atraves do xml
        if(view == null){
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            v = inflater.inflate(R.layout.item_lista_categoria, null);
        }else{
            v = view;
        }

        //mapeia os itens presentes na lista
        Categoria c = getItem(position);
        TextView itemNome = (TextView) v.findViewById(R.id.itemNome);
        itemNome.setText(c.getNome());


        return v;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}