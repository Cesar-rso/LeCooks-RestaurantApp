package com.example.lecooks;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import android.widget.AdapterView.OnItemClickListener;
import com.example.lecooks.ImageAdapter;

public class MainActivity extends Activity {
	
	GridView gridView;
	TextView txtCategoria;
	ListView ListProd;
    Context con = this;
	
	static int QTDCategoria = 0, posicaoCategoria= 0;

	static String[] Categorias = new String[] { "Carnes", "Saladas",
			"Bebidas", "Sobremesa"};
	
	static String[] Cores = new String[] {"#FF0000","#00FF00","#0000FF","#FF00FF"};
	String nomeProduto;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LeCooks/config.properties");
		Properties prop = new Properties();
		
		//lendo arquivo de configurações config.properties
		if (f.exists()){ 

	    		try {
					prop.load(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LeCooks/config.properties"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
		}else{
			prop.setProperty("QTD", "5");
			
			prop.setProperty("categoria_1", "Carnes");
			prop.setProperty("categoria_2", "Saladas");
			prop.setProperty("categoria_3", "Bebidas");
			prop.setProperty("categoria_4", "Sobremesas");
			prop.setProperty("categoria_5", "Pratos");
			
			prop.setProperty("cor_1", "#FF0000");
			prop.setProperty("cor_2", "#00FF00");
			prop.setProperty("cor_3", "#0000FF");
			prop.setProperty("cor_4", "#FFFF00");
			prop.setProperty("cor_5", "#FF00FF");
			
			try {
				prop.store(new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LeCooks/config.properties"), null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	 
				//salvando as configurações do config.properties em variaveis para serem usadas dentro da aplicação
	    		QTDCategoria = Integer.parseInt(prop.getProperty("QTD"));
	    		int cont = QTDCategoria;
	    		ArrayList<String> Categ = new ArrayList<String>();
	    		ArrayList<String> Colors = new ArrayList<String>();
	    		int cont2 = 1;
	    		while(cont>0){
	    			Categ.add(prop.getProperty("categoria_"+ cont2));
	    			Colors.add(prop.getProperty("cor_"+ cont2));
	    			cont--;
	    			cont2++;
	    		}
	    		int tamanhoCat = Categ.size();
	    		int tamanhoCor = Colors.size();
	    		final String[] Cat = new String[tamanhoCat];
	    		final String[] Cor = new String[tamanhoCor];
	    		while(cont<tamanhoCor){
	    			
	    			Cat[cont] = Categ.get(cont);
	    			Cor[cont] = Colors.get(cont);
	    			cont++;
	    		}
	 
		
		gridView = (GridView) findViewById(R.id.gridView1);
		txtCategoria = (TextView) findViewById(R.id.txtCategoria);
		
		//definindo a cor inicial do textView no topo da tela
		final ShapeDrawable shDraw = new ShapeDrawable(new RectShape());
        shDraw.getPaint().setShader(new LinearGradient(40,0,40,60,
        		Color.parseColor(Cor[0]), Color.parseColor("#FFFFFFFF"), Shader.TileMode.CLAMP));
        		//Color.parseColor(String.format("#%02x%02x%02x", 255,0,0)), Color.parseColor("#FFFFFFFF"), Shader.TileMode.CLAMP));
        txtCategoria.setBackgroundDrawable(shDraw);
        txtCategoria.setText(Cat[0]);
        	
		
		ListProd = (ListView) findViewById(R.id.ListaProd);

		gridView.setAdapter(new ImageAdapter(this, Cat, Cor));
		
		final GarcomDataSource garcom = new GarcomDataSource(this);
		garcom.open(); //abrindo o banco de dados
		
		//posicinando o Cursor num valor inicial de pesquisa 
		Cursor valuescs = garcom.getProdutosCategoriaCs(Cat[0]);

	    // Usando o adapter criado para lista os produtos do banco de dados  
	    final CListAdapter adp = new CListAdapter(this,valuescs); 
	    
	    ListProd.setAdapter(adp);
	    ListProd.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				garcom.open();
				Cursor tempcs = garcom.getProdutosCategoriaCs(Cat[posicaoCategoria]);
				tempcs.moveToPosition(position);
				String desc = tempcs.getString(tempcs.getColumnIndex("descricao"));
				nomeProduto = desc;
				String categoria = tempcs.getString(tempcs.getColumnIndex("categoria"));
				tempcs.close();
				garcom.AtualizaProdutoQTD(categoria, desc);
				adp.changeCursor(garcom.getProdutosCategoriaCs(Cat[posicaoCategoria]));
				garcom.close();

			}
	    });

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						((TextView) v.findViewById(R.id.grid_item_label))
								.getText(), (Toast.LENGTH_SHORT)/2).show();
				//alterando os dados exibidos e cor do textView do topo de acordo com o item clicado no GridView
				garcom.open();
					shDraw.getPaint().setShader(new LinearGradient(40,0,40,60,
							Color.parseColor(Cor[position]), Color.parseColor("#FFFFFFFF"), Shader.TileMode.CLAMP));
					txtCategoria.setBackgroundDrawable(shDraw);
					txtCategoria.setText(Cat[position]);
					
					adp.changeCursor(garcom.getProdutosCategoriaCs(Cat[position]));
					posicaoCategoria = position;
					
			}
		});
		garcom.close();
		
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}


	public void Complemento (View v){
		
		Intent intent = new Intent(this,Complemento.class);
		Complemento.CategoriaComplemento = txtCategoria.getText().toString();
		Complemento.nomeProduto = nomeProduto;
		startActivity(intent);
		
	}
	
	public void MenosItem (View v){
		
		GarcomDataSource gar = new GarcomDataSource(this);
		gar.open();
		gar.AtualizaProdutoMenosQTD(txtCategoria.getText().toString(), nomeProduto);
		gar.close();
	}
	
	public void OKpedido (View v){
		
		Garcom.dadosPedido = "";
		GarcomDataSource gar = new GarcomDataSource(this);
		gar.open();
		Cursor cursor = gar.getProdutosPedido();
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			String produto, prodQTD, prodCompl;
			produto = cursor.getString(cursor.getColumnIndex("_id"));
			prodQTD = cursor.getString(cursor.getColumnIndex("quantidade"));
			prodCompl = cursor.getString(cursor.getColumnIndex("complemento"));
			if(produto.length()<13){
				while(produto.length()<13){
					produto = "0" + produto;
				}
			}
			if(prodQTD.length()<4){
				while(prodQTD.length()<4){
					prodQTD = "0" + prodQTD;
				}
			}
			if(prodCompl.length()<2){
				prodCompl = "0" + prodCompl;
				if(prodCompl == "00"){
					prodCompl = "";
				}
			}
			if(Garcom.numMesa.length()<3){
				while(Garcom.numMesa.length()<3){
					Garcom.numMesa = "0" + Garcom.numMesa;
				}			
			}
			if(Garcom.numLocal.length()<3){
				while(Garcom.numLocal.length()<3){
					Garcom.numLocal = "0" + Garcom.numLocal;
				}			
			}
			if(Garcom.numComanda.length()<6){
				while(Garcom.numComanda.length()<6){
					Garcom.numComanda = "0" + Garcom.numComanda;
				}			
			}
			Garcom.dadosPedido = Garcom.dadosPedido + Garcom.numMesa + "|" + Garcom.numComanda + "|" + Garcom.numLocal + "|" + produto + "|" 
					+ prodQTD + "|" + prodCompl + "|" + Garcom.numeroAparelho + "|" + Garcom.senhaGarcom + "|" + Garcom.nomeGarcom + "\n";
			cursor.moveToNext();
		}
		cursor.close();
		gar.close();
		finish();
		
	}
	
	

}
