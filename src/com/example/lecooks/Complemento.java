package com.example.lecooks;


import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Complemento extends Activity {
	
	TextView txtProduto;
	ListView ListaComp;
	Button btnMenosComp;
	static List<String> lines = new ArrayList<String>();
    static List<String> linesCode = new ArrayList<String>();
    static List<String> linesNum = new ArrayList<String>();
    Context con = this;
    static String nomeProduto = "";	
	static String CategoriaComplemento = "";
	String codCompl, todosCodCompl;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complemento);
		
		txtProduto = (TextView) findViewById(R.id.txtProduto);
		ListaComp = (ListView) findViewById(R.id.ListaComp);
		btnMenosComp = (Button) findViewById(R.id.btnMenosComp);
		
		
		txtProduto.setText(nomeProduto);
		
		ShapeDrawable shDraw = new ShapeDrawable(new RectShape());
        shDraw.getPaint().setShader(new LinearGradient(40,0,40,60,
        		Color.parseColor(MainActivity.Cores[MainActivity.posicaoCategoria]), Color.parseColor("#FFFFFFFF"), Shader.TileMode.CLAMP));
        txtProduto.setBackgroundDrawable(shDraw);
        
        final CompDataSource compl = new CompDataSource(this);
        compl.open();
        Cursor CompCr = compl.getComplementoCat(CategoriaComplemento);
        
        final ListAdapterCompl adp = new ListAdapterCompl(this,CompCr);
        ListaComp.setAdapter(adp);
        ListaComp.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				GarcomDataSource garcom = new GarcomDataSource(con);
				garcom.open();
				Cursor curs = garcom.getProdutoComp(nomeProduto, CategoriaComplemento);
				Cursor tempCursor = compl.getComplementoCat(CategoriaComplemento);		
				tempCursor.moveToPosition(position);
				curs.moveToFirst();
				codCompl = tempCursor.getString(tempCursor.getColumnIndex("_id"));								
				compl.AtualizaComplementoQTD(codCompl);
				compl.criaComplementoProd(Integer.parseInt(codCompl), tempCursor.getString(tempCursor.getColumnIndex("descricao")), Integer.parseInt(curs.getString(curs.getColumnIndex("_id"))));
				adp.changeCursor(compl.getComplementoCat(CategoriaComplemento));
				garcom.close();
				curs.close();
				tempCursor.close();
				ListaComp.setAdapter(adp);	    					
			}        	
        });
        
        if(nomeProduto == ""){
        	AlertDialog dialog = new AlertDialog.Builder(con).create();
    		dialog.setMessage("Voce não selecionou um produto para adicionar complemento!!!");
    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					finish();
				}
			});
    		dialog.show();

        }
	}
	
	
	public void Menos(View v){
		
		CompDataSource compl = new CompDataSource(this);
		compl.open();
		compl.AtualizaComplementoMenosQTD(codCompl);
		compl.deletaComplementoProd(Integer.parseInt(codCompl));
		compl.close();
		
		
	}
	
	public void OKComp(View v){
		
		CompDataSource compl = new CompDataSource(this);
		compl.open();
		Cursor cursor = compl.getComplementoProd(CategoriaComplemento);
		cursor.moveToFirst();
		String allCod = "";
		while(!cursor.isAfterLast()){
			String codCompl;
			codCompl = cursor.getString(cursor.getColumnIndex("_id"));
			if(codCompl.length()<2){
				codCompl = "0" + codCompl;
			}
			allCod = allCod + codCompl;
			cursor.moveToNext();
		}
		cursor.close();
		compl.close();
		GarcomDataSource gar = new GarcomDataSource(this);
		gar.open();
		gar.AtualizaProdutoCompl(CategoriaComplemento, nomeProduto, allCod);
		gar.close();
		finish();
		
	}
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
