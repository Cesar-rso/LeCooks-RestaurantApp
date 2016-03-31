package com.example.lecooks;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class PrePedido extends Activity {
	
	TextView txtComanda;
	ExpandableListView listaPed;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prepedido);
		
		txtComanda = (TextView) findViewById(R.id.txtComanda);
		listaPed = (ExpandableListView) findViewById(R.id.ListaProd);
		GarcomDataSource garcom = new GarcomDataSource(this);
		garcom.open();
		Cursor cursor = garcom.getProdutosPedido();
		ListaExpAdapter ListAdp = new ListaExpAdapter(cursor,this,R.layout.single_row_item_3,
				R.layout.single_row_item_3,new String[] { "quantidade","descricao" },new int[] {R.id.PreCompl_qtd,R.id.PreCompl_descricao},
				new String[] { "descricao" },new int[] {R.id.PreCompl_descricao});
		listaPed.setAdapter(ListAdp);
		
		txtComanda.setText(txtComanda.getText() + Garcom.numComanda);
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	

}
