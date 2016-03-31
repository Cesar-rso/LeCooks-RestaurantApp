package com.example.lecooks;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ListAdapterCompl extends CursorAdapter {
	
	String prodDesc, preco;
	Cursor cursor = getCursor();
	public int[] qtdpro = new int[cursor.getCount()];
	public String[] Dadosprod = new String[cursor.getCount()];

	@SuppressWarnings("deprecation")
	public ListAdapterCompl(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		TextView textViewQTD = (TextView) view.findViewById(R.id.Compl_qtd);
        textViewQTD.setText(cursor.getString(cursor.getColumnIndex("quantidade")));
 
        TextView textViewDescricao = (TextView) view.findViewById(R.id.Compl_descricao);
        textViewDescricao.setText(cursor.getString(cursor.getColumnIndex("descricao")));
        String txtDesc = textViewDescricao.getText().toString();
        if(textViewDescricao.getText().length()>12){ //Limitando a quantidade de caracteres para não desalinhar o layout
        	textViewDescricao.setText(txtDesc.subSequence(0, 12) + "...");
        }
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// Quando a view é criada, é necessário informar como é o layout de cada item
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.single_row_item_2, parent, false);       
 
        return retView;
	}

}
