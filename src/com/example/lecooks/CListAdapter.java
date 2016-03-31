package com.example.lecooks;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CListAdapter extends CursorAdapter {
	
	String prodDesc, preco;
	Cursor cursor = getCursor();
	public int[] qtdpro = new int[cursor.getCount()];
	public String[] Dadosprod = new String[cursor.getCount()];
	
	@SuppressWarnings("deprecation")
	public CListAdapter(Context context, Cursor c) {
        super(context, c);
        int position = 0;
        while(position<qtdpro.length){
        	qtdpro[position] = 0;
        	Dadosprod[position] = "vazio";
        	position++;
        }
    }
 
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Quando a view é criada, é necessário informar como é o layout de cada item
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.single_row_item, parent, false);
        
 
        return retView;
    }
 
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Pegando a os dados do cursor e jogando na view
 
        TextView textViewQTD = (TextView) view.findViewById(R.id.Prod_qtd);
        //textViewQTD.setText(Integer.toString(qtdpro[cursor.getPosition()]));
        textViewQTD.setText(cursor.getString(cursor.getColumnIndex("quantidade")));
 
        TextView textViewDescricao = (TextView) view.findViewById(R.id.Prod_descricao);
        textViewDescricao.setText(cursor.getString(cursor.getColumnIndex("descricao")));
        String txtDesc = textViewDescricao.getText().toString();
        if(textViewDescricao.getText().length()>12){ //Limitando a quantidade de caracteres para não desalinhar o layout
        	textViewDescricao.setText(txtDesc.subSequence(0, 12) + "...");
        }
        
        TextView textViewPreco = (TextView) view.findViewById(R.id.Prod_preco);
        textViewPreco.setText(cursor.getString(cursor.getColumnIndex("preco")));
        
        Dadosprod[cursor.getPosition()] = "\n" + cursor.getString(cursor.getColumnIndex("_id")) 
        		+ "|" + cursor.getString(cursor.getColumnIndex("quantidade")) + "|" + cursor.getString(cursor.getColumnIndex("preco"));
    }

	
	public void retornaDados (int position) {	
		
		Garcom.dadosPedido = Garcom.dadosPedido + Dadosprod[position];
		qtdpro[position]++;
	}
	
    

}
