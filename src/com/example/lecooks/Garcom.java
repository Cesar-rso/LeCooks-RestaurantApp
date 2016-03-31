package com.example.lecooks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Garcom extends Activity{
	
	Button btnMesa, btnLocal, btnViagem,
	btnComanda, btnProduto, btnPrePedido,
	btnPreConta, btnConfirma, btnSair;
	
	TextView txtGarcom;
	
	String resultado;
	static String nomeGarcom, senhaGarcom, dadosPedido="", complemento="", IP, numeroAparelho, numMesa="", numLocal="", numComanda="";
	static int portaServidor;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.garcom);
		  
		  btnMesa = (Button) findViewById(R.id.btnMesa);
		  btnLocal = (Button) findViewById(R.id.btnLocal);
		  btnViagem = (Button) findViewById(R.id.btnViagem);
		  btnComanda = (Button) findViewById(R.id.btnComanda);
		  btnProduto = (Button) findViewById(R.id.btnProduto);
		  btnPrePedido = (Button) findViewById(R.id.btnPrePedido);
		  btnPreConta = (Button) findViewById(R.id.btnPreConta);
		  btnConfirma = (Button) findViewById(R.id.btnConfirma);
		  btnSair = (Button) findViewById(R.id.btnSair);
		  
		  txtGarcom = (TextView) findViewById(R.id.txtGarcom);
		  txtGarcom.setText(nomeGarcom);
		  
		  final File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LeCooks/config.properties");
			Properties prop = new Properties();
			GarcomDataSource gar = new GarcomDataSource(this);
			gar.open();
			gar.AtualizaTodosProdutos();
			gar.close();
			
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
				AlertDialog dialog = new AlertDialog.Builder(this).create();
	    		dialog.setMessage("Não foi possivel achar o arquivo de configurações!");
	    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	    		dialog.show();
			}
			IP = prop.getProperty("IP", "192.168.1.102");
			portaServidor = Integer.parseInt(prop.getProperty("porta","6880"));
			numeroAparelho = prop.getProperty("numeroAparelho", "1");
		  
	}
	
	@SuppressWarnings("deprecation")
	public void Produto (View v){
		
		if(numMesa==""||numLocal==""||numComanda==""){
			AlertDialog dialog = new AlertDialog.Builder(this).create();
    		dialog.setMessage("É necessário informar a Mesa, Local e Comanda antes de escolher o produto!");
    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
    		dialog.show();
		}else{
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
		}
		
	}
	
	public void PrePedido (View v){
		
		Intent intent = new Intent(this,PrePedido.class);
		startActivity(intent);
		
	}
	
	public void PreContaComanda (View v){
		
		Intent intent = new Intent(this,PreContaComanda.class);
		startActivity(intent);
		
	}
	
	public void numMesa (View v) {
		
		Senha.getNum = true;
		Intent intent = new Intent(this, Senha.class);
		startActivityForResult(intent,11);
		
	}
	
	public void numLocal (View v) {
		
		Senha.getNum = true;
		Intent intent = new Intent(this, Senha.class);
		startActivityForResult(intent,12);
		
	}

	public void numComanda (View v) {
		
		Senha.getNum = true;
		Intent intent = new Intent(this, Senha.class);
		startActivityForResult(intent,13);
	
	}
	
	public void Sair (View v) {
		
		Senha.getNum = true;
		Intent intent = new Intent(this, Senha.class);
		startActivityForResult(intent,33);
	}
	
	@SuppressWarnings("deprecation")
	public void ConfirmaPed (View v) {
		
		String conn = IP;
    	String msg = dadosPedido;
    	TCPClient.ip = conn;
    	TCPClient.serverPort = portaServidor;
    	TCPClient.data = msg;
    	TCPClient.ClientSocket(null);
    	
    	if (TCPClient.st.length()==TCPClient.data.length()&&TCPClient.st.equals(TCPClient.data)){
    		AlertDialog dialog = new AlertDialog.Builder(this).create();
    		dialog.setMessage("Pedido realizado com sucesso!");
    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
    		dialog.show();
    		numMesa="";
    		numLocal="";
    		numComanda="";
    		btnMesa.setText("Mesa \n000");
    		btnLocal.setText("Local \n000");
    		btnComanda.setText("Comanda \n000");
    		dadosPedido = "";
    		GarcomDataSource garcom = new GarcomDataSource(this);
    		garcom.open();
    		garcom.AtualizaTodosProdutos();
    		garcom.close();
    		CompDataSource comp = new CompDataSource(this);
    		comp.open();
    		comp.AtualizaTodosComplementos();
    		comp.deletaTodosComplementoProd();
    		comp.close();

    	}else{
    		AlertDialog dialog2 = new AlertDialog.Builder(this).create();
    		dialog2.setMessage("Erro no envio do pedido!\n Verifique se o wifi esta funcionando e tente de novo");
    		dialog2.setButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
    		dialog2.show();
    	}
			
		
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		resultado = data.getExtras().getString("valor");
		switch (requestCode){
		
		case 11:
			if(resultado.length()>3){
				AlertDialog dialog = new AlertDialog.Builder(this).create();
	    		dialog.setMessage("O número da mesa deve ser de no máximo 3 digitos!");
	    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	    		dialog.show();
			}else{
				btnMesa.setText("Mesa \n" + resultado);
				numMesa = resultado;
			}
			break;
			
		case 12:
			if(resultado.length()>3){
				AlertDialog dialog = new AlertDialog.Builder(this).create();
	    		dialog.setMessage("O número do local deve ser de no máximo 3 digitos!");
	    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	    		dialog.show();
			}else{
				btnLocal.setText("Local \n" + resultado);
				numLocal = resultado;
			}
			break;
			
		case 13:
			if(resultado.length()>6){
				AlertDialog dialog = new AlertDialog.Builder(this).create();
	    		dialog.setMessage("O número da comanda deve ser de no máximo 6 digitos!");
	    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	    		dialog.show();
			}else{
				btnComanda.setText("Comanda \n" + resultado);
				numComanda = resultado;
			}
			break;
			
		case 33:
			if(resultado.contentEquals(senhaGarcom)){
				android.os.Process.killProcess(android.os.Process.myPid());
			}else{
				AlertDialog dialog = new AlertDialog.Builder(this).create();
	    		dialog.setMessage("Senha invalida!");
	    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	    		dialog.show();
			}
			break;
		
		}
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Senha.getNum = true;
		Intent intent = new Intent(this, Senha.class);
		startActivityForResult(intent,33);
	}
	
	public void setViagem (View v){
		numMesa = "999";
		btnMesa.setText("Mesa \n999");
	}

}
